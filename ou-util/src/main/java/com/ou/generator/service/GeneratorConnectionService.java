package com.ou.generator.service;

import com.ou.generator.domain.GeneratorConnection;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.query.GeneratorConnectionQueryCriteria;
import com.ou.generator.domain.vo.GeneratorConnectionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Connection;
import java.util.List;

/**
 * @author vince
 */
public interface GeneratorConnectionService {

    /**
     *  返回所有的连接信息
     * @return  返回所有的连接信息
     */
    List<GeneratorConnectionDTO> listAll();

    /**
     *  根据条件查询连接信息
     * @param specification 查询条件实体类
     * @return 符合条件的连接信息集合
     */
    List<GeneratorConnectionDTO> listAll(Specification<GeneratorConnectionQueryCriteria> specification);

    /**
     *  分页查询连接信息
     * @param pageable 分页参数实体类
     * @return 符合条件的连接信息集合
     */
    Page<GeneratorConnection> listPageable(Pageable pageable);

    /**
     *  分页查询连接信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的连接信息集合
     */
    Page<GeneratorConnection> listPageable(Specification<GeneratorConnectionQueryCriteria> specification, Pageable pageable);

    /**
     *  根据连接id获取连接信息
     * @param id  连接id
     * @return 传入id对应的连接信息
     */
    GeneratorConnectionDTO getById(Long id);

    /**
     *  根据连接名称获取对应的连接信息
     * @param name  连接名
     * @return 传入的连接名所对应的连接信息
     */
    GeneratorConnectionDTO getByName(String name);

    /**
     *  新增连接
     * @param generatorConnectionDTO 连接信息实体
     * @return 新增成功实体类
     */
    GeneratorConnectionDTO insert(GeneratorConnectionDTO generatorConnectionDTO);

    /**
     *  删除连接
     * @param id 连接id
     */
    void delete(Long id);

    /**
     *  更新连接
     * @param generatorConnectionDTO 需要更新的连接信息
     * @return 更新成功的实体类
     */
    GeneratorConnectionDTO update(GeneratorConnectionDTO generatorConnectionDTO);

    /**
     *  检测连接id是否有误
     * @param id 连接id
     * @return 检测结果
     */
    Boolean existsById(Long id);

    /**
     *  获取sql连接
     * @param id 数据库连接的主键id
     * @return  sql连接
     */
    Connection getConnection(Long id);

    /**
     *  测试连接
     * @param id 连接的主键id
     * @return 测试连接的结果
     */
    Boolean testConnection(Long id);

    /**
     *  更新连接信息
     *  通过访问数据库, 新增/更新 数据库/表/字段 信息
     * @param id 数据库连接id
     * @return 更新结果
     */
    Boolean refresh(Long id);

    /**
     *  获取所有连接的信息包括数据库、表, 以树的形式展示
     * @return 所有连接的集合
     */
    List<GeneratorConnectionVO> listConnectionInfoTree();

}
