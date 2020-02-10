package com.ou.generator.domain.vo;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ou.generator.domain.dto.GeneratorSettingDTO;

import lombok.Data;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratorSettingVO {

    /**
     * 主键id
     */
    @NotNull(groups = GeneratorSettingDTO.Update.class, message = "主键id不能为空")
    private Long id;

    /**
     *  表id
     */
    private Long tableId;

    /**
     *  作者
     */
    private String author;

    /**
     *  所属模块
     */
    private String module;

    /**
     *  包名
     */
    private String packageName;

    /**
     *  接口名
     */
    private String interfaceName;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

}
