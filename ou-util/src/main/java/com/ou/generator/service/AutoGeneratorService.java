package com.ou.generator.service;

import com.ou.generator.domain.GeneratorSetting;

import java.util.List;

/**
 * @author vince
 */
public interface AutoGeneratorService {

    /**
     *  生成代码, 包含某个表的数据
     * @param tables 表名集合
     * @param setting 配置项
     * @return 生成结果
     */
    String generateCode(List<String> tables, GeneratorSetting setting);

}
