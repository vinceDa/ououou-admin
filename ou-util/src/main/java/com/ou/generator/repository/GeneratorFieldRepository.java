package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:55
 */
public interface GeneratorFieldRepository extends JpaRepository<GeneratorField, Long>, JpaSpecificationExecutor {

    /**
     *  根据字段名获取字段信息
     * @param name 字段名
     * @return 字段信息
     */
    GeneratorField getFieldByName(String name);

    /**
     *  获取表下的所有字段
     * @param tableId 表的主键id
     * @return 所有字段的实体类的集合
     */
    List<GeneratorField> findAllByTableId(Long tableId);

    /**
     *  获取表下的所有字段
     * @param tableIds 表的主键id集合
     * @return 所有字段的实体类的集合
     */
    List<GeneratorField> findAllByTableIdIn(List<Long> tableIds);

    /**
     *  删除某个表下的所有字段
     * @param tableIds 表的主键id集合
     */
    void deleteAllByTableIdIn(List<Long> tableIds);

    /**
     *  删除所有不在字段id集合内的字段信息
     * @param fieldIds 字段id集合
     */
    void deleteAllByIdNotIn(List<Long> fieldIds);

    /**
     *  根据表id和字段名获取字段信息
     * @param tableId 表id
     * @param name 字段名称
     * @return 符合条件的字段信息
     */
    GeneratorField getByTableIdAndName(Long tableId, String name);

}
