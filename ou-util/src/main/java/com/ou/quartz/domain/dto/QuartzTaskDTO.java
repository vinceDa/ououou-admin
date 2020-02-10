package com.ou.quartz.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author vince
 * @date 2020/02/02 07:24:34
 */
@Data
public class QuartzTaskDTO {

    /**
     *  主键id
     */
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long id;
    /**
     *  任务名
     */
    private String name;
    /**
     *  类路径
     */
    private String classPath;
    /**
     *  cron 表达式
     */
    private String cronExpression;
    /**
     *  任务调用的方法名
     */
    private String methodName;
    /**
     *  触发器名称
     */
    private String triggerName;
    /**
     *  描述
     */
    private String description;
    /**
     *  任务状态
     */
    private Boolean enable;
    /**
     *  创建人id
     */
    private Long createUserId;
    /**
     *  更新人id
     */
    private Long updateUserId;
    /**
     *  创建时间
     */
    private LocalDateTime createTime;
    /**
     *  更新时间
     */
    private LocalDateTime updateTime;

    public interface Insert{};

    public interface Update{};
}
