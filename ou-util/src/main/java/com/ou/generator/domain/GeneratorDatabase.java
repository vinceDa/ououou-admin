package com.ou.generator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "generator_database")
public class GeneratorDatabase extends BaseDO {

    /**
     *  数据库名
     */
    @Column(name = "name")
    private String name;

    /**
     *  数据库连接id
     */
    @Column(name = "connection_id")
    private Long connectionId;
/*
    @ManyToOne
    private GeneratorConnection connection;*/

}
