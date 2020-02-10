package com.ou.quartz.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.quartz.domain.dto.QuartzTaskDTO;
import com.ou.quartz.domain.query.QuartzTaskQueryCriteria;
import com.ou.quartz.domain.vo.QuartzTaskVO;
import com.ou.quartz.service.QuartzTaskService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vince
 * @date 2020/02/02 07:24:34
 */
@Slf4j
@Controller
@RequestMapping("/api/v1/util/quartz")
public class QuartzTaskController {

    @Autowired
    private QuartzTaskService quartzTaskService;

    @GetMapping(value = "/tasks")
    public ResponseEntity listAll(HttpServletRequest request, QuartzTaskQueryCriteria quartzTaskQueryCriteria) {
        List<QuartzTaskDTO> quartzTaskDTOS;
        if (BeanUtil.isEmpty(quartzTaskQueryCriteria)) {
            quartzTaskDTOS = quartzTaskService.listAll();
        } else {
            quartzTaskDTOS = quartzTaskService.listAll(quartzTaskQueryCriteria.toSpecification());
        }
        List<QuartzTaskVO> convert = Convert.convert(new TypeReference<List<QuartzTaskVO>>() {}, quartzTaskDTOS);
        return ResponseEntity.ok(convert);
    }

    @PostMapping(value = "/tasks")
    public ResponseEntity insert(HttpServletRequest request,
        @Validated(QuartzTaskDTO.Insert.class) @RequestBody QuartzTaskDTO quartzTaskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quartzTaskService.insert(quartzTaskDTO));
    }

    @PutMapping(value = "/tasks")
    public ResponseEntity put(HttpServletRequest request,
        @Validated(QuartzTaskDTO.Update.class) @RequestBody QuartzTaskDTO quartzTaskDTO) {
        quartzTaskService.update(quartzTaskDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/tasks/{id}")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        quartzTaskService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/tasks/start/{id}")
    public ResponseEntity start(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quartzTaskService.startQuartzTask(id));
    }

    @PostMapping(value = "/tasks/pause/{id}")
    public ResponseEntity pause(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quartzTaskService.pauseQuartzTask(id));
    }

    @PostMapping(value = "/tasks/resume/{id}")
    public ResponseEntity resume(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quartzTaskService.resumeQuartzTask(id));
    }
}
