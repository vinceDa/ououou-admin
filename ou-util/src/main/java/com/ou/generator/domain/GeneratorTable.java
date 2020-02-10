package com.ou.generator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ou.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "generator_table")
public class GeneratorTable extends BaseDO {

    /**
     *  数据库连接id
     */
    @Column(name = "database_id")
    private Long databaseId;

    /**
     *  表名
     */
    @Column(name = "name")
    private String name;

    /**
     *  字符集
     */
    @Column(name = "charset")
    private String charset;

    /**
     *  排序规则
     */
    @Column(name = "collation")
    private String collation;
/*
    @ManyToOne
    private GeneratorDatabase generatorDatabase;*/

}
