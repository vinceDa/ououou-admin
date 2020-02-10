package com.ou.generator.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ou.generator.domain.GeneratorTable;
import com.ou.generator.domain.dto.GeneratorTableDTO;
import com.ou.generator.domain.query.GeneratorTableQueryCriteria;

/**
 * @author vince
 */
public interface GeneratorTableService {

    /**
     *  返回所有的表信息
     * @return  返回所有的表信息
     */
    List<GeneratorTableDTO> listAll();

    /**
     *  根据条件查询表信息
     * @param specification 查询条件实体类
     * @return 符合条件的表信息集合
     */
    List<GeneratorTableDTO> listAll(Specification<GeneratorTableQueryCriteria> specification);

    /**
     *  分页查询表信息
     * @param pageable 分页参数实体类
     * @return 符合条件的表信息集合
     */
    Page<GeneratorTable> listPageable(Pageable pageable);

    /**
     *  分页查询表信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的表信息集合
     */
    Page<GeneratorTable> listPageable(Specification<GeneratorTableQueryCriteria> specification, Pageable pageable);

    /**
     *  根据表id获取表信息
     * @param id  表id
     * @return 传入id对应的表信息
     */
    GeneratorTableDTO getById(Long id);

    /**
     *  根据表名称获取对应的表信息
     * @param name  表名
     * @return 传入的表名所对应的表信息
     */
    GeneratorTableDTO getByName(String name);

    /**
     *  新增表
     * @param generatorTableDTO 表信息实体
     * @return 新增成功实体类
     */
    GeneratorTableDTO insert(GeneratorTableDTO generatorTableDTO);

    /**
     *  删除表
     * @param id 表id
     */
    void delete(Long id);

    /**
     *  更新表
     * @param generatorTableDTO 需要更新的表信息
     * @return 更新成功的实体类
     */
    GeneratorTableDTO update(GeneratorTableDTO generatorTableDTO);

    /**
     *  检测表id是否有误
     * @param id 表id
     * @return 检测结果
     */
    Boolean existsById(Long id);

}
