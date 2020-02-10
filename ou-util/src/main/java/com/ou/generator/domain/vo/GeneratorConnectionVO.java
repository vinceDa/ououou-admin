package com.ou.generator.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ou.common.base.BaseDO;
import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.dto.GeneratorDatabaseDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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
    private List<GeneratorDatabaseDTO> children;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;
}
