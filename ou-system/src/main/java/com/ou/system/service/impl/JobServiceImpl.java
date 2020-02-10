package com.ou.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.Job;
import com.ou.system.domain.dto.JobDTO;
import com.ou.system.domain.query.JobQueryCriteria;
import com.ou.system.repository.JobRepository;
import com.ou.system.security.util.SecurityUtil;
import com.ou.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author vince
 * @date 2019/10/14 23:41
 */
@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private JobRepository jobRepository;

    @Override
    public List<JobDTO> listAll() {
        List<Job> all = jobRepository.findAll();
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<JobDTO>>() {}, all);
    }

    @Override
    public List<JobDTO> listAll(Specification<JobQueryCriteria> specification) {
        List all = jobRepository.findAll(specification);
        log.info("listAllWithSpecification, size: {}", all.size());
        return Convert.convert(new TypeReference<List<JobDTO>>() {}, all);
    }

    @Override
    public Page<Job> listPageable(Pageable pageable) {
        Page<Job> all = jobRepository.findAll(pageable);
        List<Job> content = all.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return all;
    }

    @Override
    public Page<Job> listPageable(Specification<JobQueryCriteria> specification, Pageable pageable) {
        Page<Job> all = jobRepository.findAll(specification, pageable);
        List content = all.getContent();
        log.info("listAllWithSpecificationAndPageable, size: {}", content.size());
        return all;
    }

    @Override
    public JobDTO getById(Long id) {
        Job one = jobRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(JobDTO.class, one);
    }

    @Override
    public JobDTO getByName(String name) {
        Job byName = jobRepository.getByName(name);
        log.info("getByName, name: {}, byName: {}", name, byName.toString());
        return Convert.convert(JobDTO.class, byName);
    }

    @Override
    public JobDTO insert(JobDTO jobDTO) {
        Job byName = jobRepository.getByName(jobDTO.getName());
        if (byName != null) {
            log.error("insert job error: job name is exist");
            throw new BadRequestException("岗位名已存在");
        }
        Job convert = Convert.convert(Job.class, jobDTO);
        LocalDateTime now = LocalDateTime.now();
        convert.setCreateTime(now);
        convert.setUpdateTime(now);
        Long userId = SecurityUtil.getJwtUserId();
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        Job save = jobRepository.save(convert);
        log.info("insert, save: {}", save.toString());
        return Convert.convert(JobDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!jobRepository.existsById(id)) {
            log.error("delete job error: unknown id");
            throw new BadRequestException("请选择岗位删除");
        }
        jobRepository.deleteById(id);
    }

    @Override
    public JobDTO update(JobDTO jobDTO) {
        Optional<Job> byId = jobRepository.findById(jobDTO.getId());
        if (!byId.isPresent()) {
            log.error("update job error: unknown id");
            throw new BadRequestException("请选择岗位编辑");
        }
        if (StrUtil.isNotBlank(jobDTO.getName())) {
            Job byName = jobRepository.getByName(jobDTO.getName());
            if (byName != null && !byName.getId().equals(jobDTO.getId())) {
                log.error("update job error: iobName is exist");
                throw new BadRequestException("岗位名已存在");
            }
        }
        Job one = byId.get();
        Job convert = Convert.convert(Job.class, jobDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        Job save = jobRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(JobDTO.class, save);
    }

    @Override
    public List<JobDTO> listJobs(Long departmentId) {
        List<Job> jobs = jobRepository.findJobByDepartmentIdAndEnableIsTrue(departmentId);
        log.info("listJobs, size: {}", jobs.size());
        return Convert.convert(new TypeReference<List<JobDTO>>() {}, jobs);
    }
}
