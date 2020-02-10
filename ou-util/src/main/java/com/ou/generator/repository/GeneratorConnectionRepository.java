package com.ou.generator.repository;

import com.ou.generator.domain.GeneratorConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author vince
 * @date 2019/12/6 16:55
 */
public interface GeneratorConnectionRepository extends JpaRepository<GeneratorConnection, Long>, JpaSpecificationExecutor {

    /**
     *  根据连接名获取连接信息
     * @param name 连接名
     * @return 连接信息
     */
    GeneratorConnection getConnectionByName(String name);

}
