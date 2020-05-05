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
@Table(name = "generator_setting")
public class GeneratorSetting extends BaseDO {

    /**
     *  表id
     */
    @Column(name = "table_id")
    private Long tableId;

    /**
     *  作者
     */
    @Column(name = "author")
    private String author;

    /**
     *  所属模块
     */
    @Column(name = "module")
    private String module;

    /**
     *  包名
     */
    @Column(name = "package")
    private String packageName;

    /**
     *  接口路径
     */
    @Column(name = "interface")
    private String interfaceName;

}
