package com.ou.generator.domain;

import lombok.Data;

@Data
public class GeneratorContent {

    /**
     *  模板名称
     */
    private String templateName;

    /**
     *  生成文件所在包名
     */
    private String packageName;

    /**
     *  文件名后缀
     */
    private String suffix;

    /**
     *  文件类型
     */
    private String fileType;

}
