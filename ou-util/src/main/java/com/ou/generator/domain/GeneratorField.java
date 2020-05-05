package com.ou.generator.domain;

import com.ou.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "generator_field")
public class GeneratorField extends BaseDO {

    /**
     *  表id
     */
    @Column(name = "table_id")
    private Long tableId;

    /**
     *  字段名
     */
    @Column(name = "name")
    private String name;

    /**
     *  字段类型
     */
    @Column(name = "type")
    private String type;

    /**
     *  是否必填
     */
    @Column(name = "is_required")
    private Boolean isRequired;

    /**
     *  字段描述
     */
    @Column(name = "comment")
    private String comment;

    /**
     *  查询方式, 不为空则该字段为查询条件
     */
    @Column(name = "query_type")
    private Boolean queryType;
/*
    @ManyToOne
    private GeneratorTable generatorTable;*/

}
