package ${package}.common.retention;

import ${package}.common.constant.sql.ConnectConditionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  查询字段的属性描述
 * @author ${author}
 * @date ${date}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryField {

    /**
     *  数据库字段名, 默认为空即默认Query实体的字段要与数据库的字段一致, 各个字段的column用and连接查询
     */
    String column() default "";

    /**
     *  模糊查询的字段, 默认为空数组即默认Query实体的字段要与数据库的字段一致, 数组中的元素用or连接查询
     */
    String[] blurry() default {};

    /**
     *  查询的连接条件, 例: equal, like, gt, lt...
     */
    ConnectConditionEnum connect() default ConnectConditionEnum.EQUAL;

}
