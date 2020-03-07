package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
     *  删除所有不在表id集合中的数据库信息
     * @param tableIds 表id集合
     */
    void deleteAllByIdNotIn(List<Long> tableIds);

    /**
     *  根据数据库id和表名获取表信息
     * @param databaseId 数据库id
     * @param name 表名
     * @return 符合条件的表信息
     */
    GeneratorTable getByDatabaseIdAndName(Long databaseId, String name);

}
