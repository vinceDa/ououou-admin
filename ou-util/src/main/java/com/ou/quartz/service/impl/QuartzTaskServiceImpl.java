package com.ou.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.quartz.domain.QuartzTask;
import com.ou.quartz.domain.dto.QuartzTaskDTO;
import com.ou.quartz.domain.query.QuartzTaskQueryCriteria;
import com.ou.quartz.repository.QuartzTaskRepository;
import com.ou.quartz.service.QuartzTaskService;
import com.ou.quartz.util.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author vince
 * @date 2020/02/02 07:24:34
 */
@Slf4j
@Service
public class QuartzTaskServiceImpl implements QuartzTaskService {

    /**
     *  注入任务调度
     */
    @Autowired
    private Scheduler scheduler;

    @Resource
    private QuartzTaskRepository quartzTaskRepository;

    @Override
    public List<QuartzTaskDTO> listAll() {
        List<QuartzTask> all = quartzTaskRepository.findAll();
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<QuartzTaskDTO>>() {}, all);
    }

    @Override
    public List<QuartzTaskDTO> listAll(Specification<QuartzTaskQueryCriteria> specification) {
        List<QuartzTaskDTO> all = quartzTaskRepository.findAll(specification);
        log.info("listAllWithSpecification, size: {}", all.size());
        return Convert.convert(new TypeReference<List<QuartzTaskDTO>>() {}, all);
    }

    @Override
    public Page<QuartzTask> listPageable(Pageable pageable) {
        Page<QuartzTask> page = quartzTaskRepository.findAll(pageable);
        List<QuartzTask> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public Page<QuartzTask> listPageable(Specification<QuartzTaskQueryCriteria> specification, Pageable pageable) {
        Page<QuartzTask> page = quartzTaskRepository.findAll(specification, pageable);
        List<QuartzTask> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public QuartzTaskDTO getById(Long id) {
        QuartzTask one = quartzTaskRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(QuartzTaskDTO.class, one);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuartzTaskDTO insert(QuartzTaskDTO quartzTaskDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        QuartzTask convert = Convert.convert(QuartzTask.class, quartzTaskDTO);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        convert.setEnable(true);
        QuartzTask save = quartzTaskRepository.save(convert);

        log.info("insert, save: {}", save.toString());
        return Convert.convert(QuartzTaskDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!quartzTaskRepository.existsById(id)) {
            log.error("delete quartzTask error: unknown id");
        }

        quartzTaskRepository.deleteById(id);
    }

    @Override
    public QuartzTaskDTO update(QuartzTaskDTO quartzTaskDTO) {
        Optional<QuartzTask> byId = quartzTaskRepository.findById(quartzTaskDTO.getId());
        if (!byId.isPresent()) {
            log.error("update quartzTask error: unknown id");
        }
        QuartzTask one = byId.get();
        QuartzTask convert = Convert.convert(QuartzTask.class, quartzTaskDTO);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        QuartzTask save = quartzTaskRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(QuartzTaskDTO.class, save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startQuartzTask(Long id) {
        QuartzTask one = quartzTaskRepository.getOne(id);
        QuartzUtils.createScheduleJob(scheduler, one);
        return true;
    }

    @Override
    public Boolean pauseQuartzTask(Long id) {
        QuartzTask one = quartzTaskRepository.getOne(id);
        QuartzUtils.pauseScheduleJob(scheduler, one.getName());
        return true;
    }

    @Override
    public Boolean resumeQuartzTask(Long id) {
        QuartzTask one = quartzTaskRepository.getOne(id);
        QuartzUtils.resumeScheduleJob(scheduler, one.getName());
        return true;
    }

}
