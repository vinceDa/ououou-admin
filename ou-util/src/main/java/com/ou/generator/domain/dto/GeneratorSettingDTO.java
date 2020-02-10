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
public class GeneratorSettingDTO {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class, message = "主键id不能为空")
    private Long id;

    /**
     *  表id
     */
    @NotNull(groups = Update.class, message = "表id不能为空")
    private Long tableId;

    /**
     *  作者
     */
    @NotEmpty(groups = Insert.class, message = "作者不能为空")
    private String author;

    /**
     *  所属模块
     */
    /*@NotEmpty(groups = Insert.class, message = "所属模块不能为空")*/
    private String module;

    /**
     *  包名
     */
    @NotEmpty(groups = Insert.class, message = "包名不能为空")
    private String packageName;

    /**
     *  接口名
     */
    @NotEmpty(groups = Insert.class, message = "接口名不能为空")
    private String interfaceName;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    public interface Insert {
    }

    public interface Update {
    }

}
