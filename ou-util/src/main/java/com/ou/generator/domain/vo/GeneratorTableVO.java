package com.ou.generator.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratorTableVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     *  数据库id
     */
    private Long databaseId;

    /**
     *  数据库名
     */
    private String databaseName;

    /**
     *  表名
     */
    private String name;

    /**
     *  字符集
     */
    private String charset;

    /**
     *  排序规则
     */
    private String collation;

    /**
     *  生成树时需要的uniqueKey
     */
    private String key;

    /**
     *  前端使用, 和name值一样
     */
    private String title;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;
}
