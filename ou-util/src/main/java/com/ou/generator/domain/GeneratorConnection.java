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
@Table(name = "generator_connection")
public class GeneratorConnection extends BaseDO {

    /**
     *  连接名
     */
    @Column(name = "name")
    private String name;

    /**
     *  连接类型, 暂时只支持MySQL
     */
    @Column(name = "type")
    private Integer type;

    /**
     *  主机名, 即ip
     */
    @Column(name = "host")
    private String host;

    /**
     *  端口
     */
    @Column(name = "port")
    private String port;

    /**
     *  用户名
     */
    @Column(name = "username")
    private String username;

    /**
     *  密码
     */
    @Column(name = "password")
    private String password;

    /**
     *  连接状态: 0失败, 1成功
     */
    @Column(name = "enable")
    private Boolean enable;
}
