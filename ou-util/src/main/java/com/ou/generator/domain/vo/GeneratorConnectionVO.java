package com.ou.generator.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratorConnectionVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     *  前端使用, 和name值一样
     */
    private String title;

    /**
     *  前端数据唯一标识
     */
    private String key;

    /**
     *  连接名
     */
    private String name;

    /**
     *  连接类型, 暂时只支持MySQL
     */
    private Integer type;

    /**
     *  主机名, 即ip
     */
    private String host;

    /**
     *  端口
     */
    private String port;

    /**
     *  用户名
     */
    private String username;

    /**
     *  密码
     */
    private String password;

    /**
     *  连接状态: 0失败, 1成功
     */
    private Boolean enable;

    /**
     *  连接下的数据库
     */
    private List<GeneratorDatabaseVO> children;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;
}
