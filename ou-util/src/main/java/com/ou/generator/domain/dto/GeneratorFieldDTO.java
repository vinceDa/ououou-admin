package com.ou.generator.domain.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
public class GeneratorFieldDTO {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class, message = "字段id不能为空")
    private Long id;

    /**
     *  字段名
     */
    @NotEmpty(groups = Insert.class, message = "字段名不能为空")
    private String name;

    /**
     *  表id
     */
    @NotNull(groups = Insert.class, message = "表id不能为空")
    private Long tableId;

    /**
     *  字段类型
     */
    private String type;

    /**
     *  字段长度
     */
    private String length;

    /**
     *  是否必填
     */
    private Boolean isRequired;

    /**
     *  字段描述
     */
    private String comment;

    /**
     *  查询方式, 不为空则该字段为查询条件
     */
    private Boolean queryType;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    public interface Insert {
    }

    public interface Update {
    }
}
