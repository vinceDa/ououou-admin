package ${package}.common.base;

import cn.hutool.core.util.StrUtil;
import ${package}.common.constant.sql.ConnectConditionEnum;
import ${package}.common.retention.QueryField;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *  查询类的基类
 * @author ${author}
 * @date ${date}
 */
@Data
public abstract class BaseQuery<T> {

    /**
     *  将查询转换为Specification
     * @return Specification实体
     */
    public abstract Specification<T> toSpecification();

    protected Specification<T> toSpecificationWithAnd() {
        return this.toSpecificationWithLogicType("and");
    }

    public Specification<T> toSpecificationWithOr() {
        return this.toSpecificationWithLogicType("or");
    }

    /**
     *  根据逻辑类型生成Specification实体
     * @param logicType  and/or...
     * @return Specification实体
     */
    private Specification<T> toSpecificationWithLogicType(String logicType) {
        BaseQuery query = this;
        return (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            // 获取查询类的所有字段, 包括父类
            List<Field> fields = listAllFieldWithRoot(query.getClass());
            List<Predicate> predicates = new ArrayList<>(fields.size());
            for (Field single : fields) {
                // 拿到字段上的@QueryField注解
                QueryField queryField = single.getAnnotation(QueryField.class);
                if (queryField == null) {
                    continue;
                }
                single.setAccessible(true);
                try {
                    Object value = single.get(query);
                    if (value == null) {
                        continue;
                    }
                    String column = queryField.column();
                    String[] blurryArray = queryField.blurry();
                    ConnectConditionEnum connect = queryField.connect();
                    Path path;
                    // 模糊查询
                    if (blurryArray.length != 0) {
                        List<Predicate> orPredicates = new ArrayList<>();
                        for (String blurry : blurryArray) {
                            orPredicates.add(criteriaBuilder.like(root.get(blurry), "%" + value + "%"));
                        }
                        Predicate[] predicateArray = new Predicate[orPredicates.size()];
                        predicates.add(criteriaBuilder.or(orPredicates.toArray(predicateArray)));
                        continue;
                    }
                    if (!column.isEmpty()) {
                        path = root.get(column);
                        switch (connect) {
                            case EQUAL:
                                predicates.add(criteriaBuilder.equal(path, value));
                                break;
                            case GT:
                                predicates.add(criteriaBuilder.gt(path, (Number) value));
                                break;
                            case GE:
                                predicates.add(criteriaBuilder.ge(path, (Number) value));
                                break;
                            case LT:
                                predicates.add(criteriaBuilder.lt(path, (Number) value));
                                break;
                            case LE:
                                predicates.add(criteriaBuilder.le(path, (Number) value));
                                break;
                            case NOT_EQUAL:
                                predicates.add(criteriaBuilder.notEqual(path, value));
                                break;
                            case LIKE:
                                predicates.add(criteriaBuilder.like(path, "%" + value + "%"));
                                break;
                            case NOT_LIKE:
                                predicates.add(criteriaBuilder.notLike(path, "%" + value + "%"));
                                break;
                            case GREATER_THAN:
                                predicates.add(criteriaBuilder.greaterThan(path, (Comparable) value));
                                break;
                            case GREATER_THAN_OR_EQUAL_TO:
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                                break;
                            case LESS_THAN:
                                predicates.add(criteriaBuilder.lessThan(path, (Comparable) value));
                                break;
                            case LESS_THAN_OR_EQUAL_TO:
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value));
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Predicate predicate = null;
            // and连接
            if (StrUtil.isEmpty(logicType) || "and".equals(logicType)) {
                predicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                // or连接
            } else if ("or".equals(logicType)) {
                predicate = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
            return predicate;
        };
    }

    /**
     *  找到类所有字段包括父类的集合
     * @param clazz 类Class
     * @return 类所有字段的集合
     */
    private List<Field> listAllFieldWithRoot(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length != 0) {
            fieldList.addAll(Arrays.asList(fields));
        }
        Class<?> superclass = clazz.getSuperclass();
        // 如果父类是Object, 直接返回
        if (superclass == Object.class) {
            return fieldList;
        }
        // 递归获取所有的父级的Field
        List<Field> superClassFieldList = listAllFieldWithRoot(superclass);
        if (!superClassFieldList.isEmpty()) {
            superClassFieldList.stream()
                    // 去除重复字段
                    .filter(field -> !fieldList.contains(field))
                    .forEach(fieldList::add);

        }
        return fieldList;
    }
}
