package com.ou.generator.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.dto.GeneratorDatabaseDTO;
import com.ou.generator.domain.query.GeneratorDatabaseQueryCriteria;

/**
 * @author vince
 */
public interface GeneratorDatabaseService {

    /**
     *  返回所有的数据库信息
     * @return  返回所有的数据库信息
     */
    List<GeneratorDatabaseDTO> listAll();

    /**
     *  根据条件查询数据库信息
     * @param specification 查询条件实体类
     * @return 符合条件的数据库信息集合
     */
    List<GeneratorDatabaseDTO> listAll(Specification<GeneratorDatabaseQueryCriteria> specification);

    /**
     *  分页查询数据库信息
     * @param pageable 分页参数实体类
     * @return 符合条件的数据库信息集合
     */
    Page<GeneratorDatabase> listPageable(Pageable pageable);

    /**
     *  分页查询数据库信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的数据库信息集合
     */
    Page<GeneratorDatabase> listPageable(Specification<GeneratorDatabaseQueryCriteria> specification, Pageable pageable);

    /**
     *  根据数据库id获取数据库信息
     * @param id  数据库id
     * @return 传入id对应的数据库信息
     */
    GeneratorDatabaseDTO getById(Long id);

    /**
     *  根据数据库名称获取对应的数据库信息
     * @param name  数据库名
     * @return 传入的数据库名所对应的数据库信息
     */
    GeneratorDatabaseDTO getByName(String name);

    /**
     *  新增数据库
     * @param generatorDatabaseDTO 数据库信息实体
     * @return 新增成功实体类
     */
    GeneratorDatabaseDTO insert(GeneratorDatabaseDTO generatorDatabaseDTO);

    /**
     *  删除数据库
     * @param id 数据库id
     */
    void delete(Long id);

    /**
     *  更新数据库
     * @param generatorDatabaseDTO 需要更新的数据库信息
     * @return 更新成功的实体类
     */
    GeneratorDatabaseDTO update(GeneratorDatabaseDTO generatorDatabaseDTO);

    /**
     *  检测数据库id是否有误
     * @param id 数据库id
     * @return 检测结果
     */
    Boolean existsById(Long id);

}
