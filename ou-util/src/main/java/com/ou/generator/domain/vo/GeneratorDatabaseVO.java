package com.ou.generator.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratorDatabaseVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     *  数据库连接id
     */
    private Long connectionId;

    /**
     *  数据库连接名
     */
    private String connectionName;

    /**
     *  数据库名
     */
    private String name;

    /**
     *  生成树时需要的uniqueKey
     */
    private String key;

    /**
     *  前端使用, 和name值一样
     */
    private String title;

    /**
     *  数据库下的表
     */
    private List<GeneratorTableVO> children;
}
