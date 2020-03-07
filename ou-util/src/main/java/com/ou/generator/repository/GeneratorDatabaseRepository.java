package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:55
 */
public interface GeneratorDatabaseRepository extends JpaRepository<GeneratorDatabase, Long>, JpaSpecificationExecutor {

    /**
     *  根据数据库名获取数据库信息
     * @param name 数据库名
     * @return 数据库信息
     */
    GeneratorDatabase getDatabaseByName(String name);

    /**
     *  获取某个连接下的所有数据库
     * @param connectionId 连接的主键id集合
     * @return 数据库的实体类的集合
     */
    List<GeneratorDatabase> findAllByConnectionId(Long connectionId);

    /**
     *  删除某个连接下的所有数据库
     * @param connectionId 连接的主键id
     */
    void deleteAllByConnectionId(Long connectionId);

    /**
     *  删除所有不在数据库id集合中的数据库信息
     * @param databaseIds 数据库id的集合
     */
    void deleteAllByIdNotIn(List<Long> databaseIds);

    /**
     *  根据连接id和数据库名称查找数据库信息
     * @param connectionId 连接id
     * @param name 数据库名称
     * @return 符合条件的数据库信息
     */
    GeneratorDatabase getByConnectionIdAndAndName(Long connectionId, String name);
}
