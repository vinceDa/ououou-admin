package com.ou.generator.domain.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
public class GeneratorTableDTO {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class, message = "表id不能为空")
    private Long id;

    /**
     *  数据库id
     */
    @NotNull(groups = Insert.class, message = "数据库id不能为空")
    private Long databaseId;

    /**
     *  数据库名
     */
    @NotEmpty(groups = Insert.class, message = "数据库名不能为空")
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

    public interface Insert {
    }

    public interface Update {
    }
}
