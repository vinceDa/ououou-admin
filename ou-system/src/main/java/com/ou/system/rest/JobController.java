package com.ou.system.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import com.ou.system.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.Job;
import com.ou.system.domain.dto.DepartmentDTO;
import com.ou.system.domain.dto.JobDTO;
import com.ou.system.domain.query.JobQueryCriteria;
import com.ou.system.domain.vo.JobVO;
import com.ou.system.service.DepartmentService;
import com.ou.system.service.JobService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/system")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/jobs")
    // @PreAuthorize("job")
    public ResponseEntity listAll(HttpServletRequest request, JobQueryCriteria jobQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        jobQueryCriteria.setCreateUserId(userId);
        List<JobDTO> jobDTOS;
        if (BeanUtil.isEmpty(jobQueryCriteria)) {
            jobDTOS = jobService.listAll();
        } else {
            jobDTOS = jobService.listAll(jobQueryCriteria.toSpecification());
        }
        List<JobVO> convert = Convert.convert(new TypeReference<List<JobVO>>() {
        }, jobDTOS);
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/jobs/paging")
    // @PreAuthorize("job_list")
    public ResponseEntity listForPage(HttpServletRequest request, JobQueryCriteria jobQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        jobQueryCriteria.setCreateUserId(userId);
        Page<Job> jobs;
        if (BeanUtil.isEmpty(jobQueryCriteria)) {
            jobs = jobService.listPageable(pageable);
        } else {
            jobs = jobService.listPageable(jobQueryCriteria.toSpecification(), pageable);
        }
        List<Job> content = jobs.getContent();
        List<JobVO> convert = Convert.convert(new TypeReference<List<JobVO>>() {
        }, content);
        for (JobVO single : convert) {
            if (single.getDepartmentId() != null) {
                DepartmentDTO byId = departmentService.getById(single.getDepartmentId());
                if (byId == null) {
                    log.warn("job listForPage, error departmentId: {}", single.getDepartmentId());
                } else {
                    single.setDepartmentName(byId.getName());
                }
            }
        }
        Page<JobVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(jobs.getNumber(), jobs.getSize()), jobs.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @PostMapping(value = "/jobs")
    // @PreAuthorize("job_add")
    public ResponseEntity insert(@Validated(JobDTO.Insert.class) @RequestBody JobDTO jobDTO) {
        if (jobDTO.getDepartmentId() != null) {
            Boolean existsById = departmentService.existsById(jobDTO.getDepartmentId());
            if (!existsById) {
                log.error("insert job error: unknown departmentId");
                throw new BadRequestException("未知的部门");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.insert(jobDTO));
    }

    @PutMapping(value = "/jobs")
    // @PreAuthorize("job_update")
    public ResponseEntity put(@Validated(JobDTO.Update.class) @RequestBody JobDTO jobDTO) {
        Boolean existsById = departmentService.existsById(jobDTO.getDepartmentId());
        if (!existsById) {
            log.error("update job error: unknown departmentId");
            throw new BadRequestException("未知的部门");
        }
        jobService.update(jobDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/jobs/{id}")
    // @PreAuthorize("job_delete")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        jobService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/departments/{id}/jobs")
    public ResponseEntity listJob(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(jobService.listJobs(id));
    }
}
