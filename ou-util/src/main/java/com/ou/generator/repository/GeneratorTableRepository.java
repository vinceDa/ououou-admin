package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ou.generator.domain.GeneratorTable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:55
 */
public interface GeneratorTableRepository extends JpaRepository<GeneratorTable, Long>, JpaSpecificationExecutor {

    /**
     *  根据表名获取表信息
     * @param name 表名
     * @return 表信息
     */
    GeneratorTable getTableByName(String name);

    /**
     *  获取数据库下的所有表
     * @param databaseIds 数据库的主键id集合
     * @return 所有表的实体类的集合
     */
    List<GeneratorTable> findAllByDatabaseIdIn(List<Long> databaseIds);

    /**
     *  删除某个数据库下的所有表
     * @param databaseIds 数据库的主键id集合
     */
    void deleteAllByDatabaseIdIn(List<Long> databaseIds);

    /**
     *  查询数据库下的所有表
     * @param databaseName 数据库名
     * @return 表的实体集合
     */
    @Query(value = "select TABLE_NAME name, TABLE_COLLATION colloation from information_schema.tables  where table_schema = ?", nativeQuery = true)
    List<GeneratorTable> listAll(String databaseName);

}
