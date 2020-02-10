package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ou.generator.domain.GeneratorField;
import org.springframework.data.jpa.repository.Query;

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
     *  查询表下的所有字段
     * @param tableName 表名
     * @param databaseName 数据库名
     * @return 表的实体集合
     */
    @Query(value = "select COLUMN_NAME name, COLUMN_TYPE type, IS_NULLABLE is_required, COLUMN_COMMENT comment " +
            "from information_schema.COLUMNS where table_name = ? and table_schema = ?;", nativeQuery = true)
    List<GeneratorField> listAll(String tableName, String databaseName);

}
