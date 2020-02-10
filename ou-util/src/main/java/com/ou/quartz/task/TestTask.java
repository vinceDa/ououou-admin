package com.ou.quartz.task;

import com.ou.quartz.domain.dto.QuartzTaskDTO;
import com.ou.quartz.service.QuartzTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author vince
 * @date 2020/2/3 13:30
 */
@Slf4j
public class TestTask extends QuartzJobBean {

    @Autowired
    private QuartzTaskService quartzTaskService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        QuartzTaskDTO byId = quartzTaskService.getById(4L);
        log.info("test task: {}", byId);

    }
}
