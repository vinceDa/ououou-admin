package com.ou.generator.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ou.generator.domain.GeneratorField;
import com.ou.generator.domain.dto.GeneratorFieldDTO;
import com.ou.generator.domain.query.GeneratorFieldQueryCriteria;

/**
 * @author vince
 */
public interface GeneratorFieldService {

    /**
     *  返回所有的字段信息
     * @return  返回所有的字段信息
     */
    List<GeneratorFieldDTO> listAll();

    /**
     *  根据条件查询字段信息
     * @param specification 查询条件实体类
     * @return 符合条件的字段信息集合
     */
    List<GeneratorFieldDTO> listAll(Specification<GeneratorFieldQueryCriteria> specification);

    /**
     *  分页查询字段信息
     * @param pageable 分页参数实体类
     * @return 符合条件的字段信息集合
     */
    Page<GeneratorField> listPageable(Pageable pageable);

    /**
     *  分页查询字段信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的字段信息集合
     */
    Page<GeneratorField> listPageable(Specification<GeneratorFieldQueryCriteria> specification, Pageable pageable);

    /**
     *  根据字段id获取字段信息
     * @param id  字段id
     * @return 传入id对应的字段信息
     */
    GeneratorFieldDTO getById(Long id);

    /**
     *  根据字段名称获取对应的字段信息
     * @param name  字段名
     * @return 传入的字段名所对应的字段信息
     */
    GeneratorFieldDTO getByName(String name);

    /**
     *  新增字段
     * @param generatorFieldDTO 字段信息实体
     * @return 新增成功实体类
     */
    GeneratorFieldDTO insert(GeneratorFieldDTO generatorFieldDTO);

    /**
     *  删除字段
     * @param id 字段id
     */
    void delete(Long id);

    /**
     *  更新字段
     * @param generatorFieldDTO 需要更新的字段信息
     * @return 更新成功的实体类
     */
    GeneratorFieldDTO update(GeneratorFieldDTO generatorFieldDTO);

    /**
     *  检测字段id是否有误
     * @param id 字段id
     * @return 检测结果
     */
    Boolean existsById(Long id);

    /**
     *  根据表id获取字段信息
     * @param tableId 表id
     * @return 字段信息的集合
     */
    List<GeneratorFieldDTO> listFieldsByTableId(Long tableId);

}
