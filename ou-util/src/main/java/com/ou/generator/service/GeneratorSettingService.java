package com.ou.generator.service;

import java.util.List;

import com.ou.generator.domain.dto.GeneratorSettingDTO;

/**
 * @author vince
 */
public interface GeneratorSettingService {

    /**
     *  根据配置id获取配置信息
     * @param id  配置id
     * @return 传入id对应的配置信息
     */
    GeneratorSettingDTO getById(Long id);

    /**
     *  新增配置
     * @param generatorSettingDTO 配置信息实体
     * @return 新增成功实体类
     */
    GeneratorSettingDTO insert(GeneratorSettingDTO generatorSettingDTO);

    /**
     *  删除配置
     * @param id 配置id
     */
    void delete(Long id);

    /**
     *  更新配置
     * @param generatorSettingDTO 需要更新的配置信息
     * @return 更新成功的实体类
     */
    GeneratorSettingDTO update(GeneratorSettingDTO generatorSettingDTO);

    /**
     *  检测配置id是否有误
     * @param id 配置id
     * @return 检测结果
     */
    Boolean existsById(Long id);

    /**
     *  根据表id获取配置信息
     * @param tableId 表id
     * @return 配置信息
     */
    GeneratorSettingDTO listSettingsByTableId(Long tableId);

}
