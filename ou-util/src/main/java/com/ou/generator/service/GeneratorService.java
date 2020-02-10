package com.ou.generator.service;

import java.util.List;

/**
 * @author vince
 */
public interface GeneratorService {

    /**
     *  生成代码, 包含某个表的数据
     * @param tableIds 表的主键id集合
     * @param projectName 项目名
     * @return 生成结果
     */
    String generateCode(List<Long> tableIds, String projectName);

}
