package com.ou.quartz.util;

import com.ou.common.exception.BadRequestException;
import com.ou.quartz.domain.QuartzTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @author vince
 * @date 2020/2/1 20:42
 */
@Slf4j
public class QuartzUtils {


    /**
     * 创建定时任务 定时任务创建之后默认启动状态
     * @param scheduler   调度器
     * @param quartzTask  定时任务信息类
     */
    public static void createScheduleJob(Scheduler scheduler, QuartzTask quartzTask){
        try {
            // 获取到定时任务的执行类  必须是类的绝对路径名称
            // 定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(quartzTask.getClassPath());
            // 构建定时任务信息
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(quartzTask.getName()).build();
            // 设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTask.getCronExpression());
            // 构建触发器trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(quartzTask.getName()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            log.error("bean class path error: {}", e.toString());
            throw new BadRequestException("定时任务类路径出错：请输入类的绝对路径");
        } catch (SchedulerException e) {
            log.error("create task error: {}", e.toString());
            throw new BadRequestException("创建定时任务出错");
        }
    }

    /**
     * 根据任务名称暂停定时任务
     * @param scheduler  调度器
     * @param jobName    定时任务名称
     * @throws SchedulerException
     */
    public static void pauseScheduleJob(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("pause task error: {}", e.toString());
            throw new BadRequestException("暂停定时任务出错");
        }
    }

    /**
     * 根据任务名称恢复定时任务
     * @param scheduler  调度器
     * @param jobName    定时任务名称
     * @throws SchedulerException
     */
    public static void resumeScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("resume task error: {}", e.toString());
            throw new BadRequestException("恢复定时任务出错");
        }
    }

    /**
     * 根据任务名称立即运行一次定时任务
     * @param scheduler     调度器
     * @param jobName       定时任务名称
     * @throws SchedulerException
     */
    public static void runOnce(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("runOnce task error: {}", e.toString());
            throw new BadRequestException("立即运行定时任务出错");
        }
    }

    /**
     * 更新定时任务
     * @param scheduler   调度器
     * @param quartzTask  定时任务信息类
     * @throws SchedulerException
     */
    public static void updateScheduleJob(Scheduler scheduler, QuartzTask quartzTask)  {
        try {
            //获取到对应任务的触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(quartzTask.getName());
            //设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTask.getCronExpression());
            //重新构建任务的触发器trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置对应的job
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("update task error: {}", e.toString());
            throw new BadRequestException("更新定时任务出错");
        }
    }

    /**
     * 根据定时任务名称从调度器当中删除定时任务
     * @param scheduler 调度器
     * @param jobName   定时任务名称
     * @throws SchedulerException
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("delete task error: {}", e.toString());
            throw new BadRequestException("删除定时任务出错");
        }
    }

}
