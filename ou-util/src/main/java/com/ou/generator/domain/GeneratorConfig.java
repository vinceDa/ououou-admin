package com.ou.generator.domain;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorConfig {

    /**
     *  模板所在路径
     */
    private String freemarkerPath;

    /**
     *  包名前缀，一般为src.main.java
     */
    private String packagePrefix;

    /**
     *  具体内容配置
     */
    private List<GeneratorContent> content;
}
