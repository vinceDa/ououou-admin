package com.ou.generator.domain.dto;

import com.ou.generator.domain.GeneratorDatabase;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
public class GeneratorConnectionDTO {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class, message = "连接id不能为空")
    private Long id;

    /**
     * 连接名
     */
    @NotEmpty(groups = Insert.class, message = "连接名不能为空")
    private String name;

    /**
     * 连接类型, 暂时只支持MySQL
     */
    /*@NotNull(groups = Insert.class, message = "连接类型不能为空")*/
    private Integer type;

    /**
     * 主机名, 即ip
     */
    @NotEmpty(groups = Insert.class, message = "主机名不能为空")
    private String host;

    /**
     * 端口
     */
    @NotEmpty(groups = Insert.class, message = "端口不能为空")
    private String port;

    /**
     * 用户名
     */
    @NotEmpty(groups = Insert.class, message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(groups = Insert.class, message = "密码不能为空")
    private String password;

    /**
     * 连接状态: 0失败, 1成功
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

    /**
     *  更新时间
     */
    private LocalDateTime updateTime;

    public interface Insert {
    }

    public interface Update {
    }

}
