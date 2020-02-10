package com.ou.generator.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratorFieldVO {

    /**
     *  表id
     */
    private Long tableId;

    /**
     *  字段名
     */
    private String name;

    /**
     *  实体类的字段名(小驼峰)
     */
    private String humpName;

    /**
     *  字段类型
     */
    private String type;

    /**
     *  包装类型, Boolean/String...
     */
    private String packagingType;

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
}
