package com.ou.generator.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.dto.GeneratorDatabaseDTO;
import com.ou.generator.domain.query.GeneratorDatabaseQueryCriteria;
import com.ou.generator.domain.vo.GeneratorDatabaseVO;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorDatabaseService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/util/generate")
public class GeneratorDatabaseController {

    @Autowired
    private GeneratorDatabaseService generatorDatabaseService;

    @GetMapping(value = "/databases")
    @PreAuthorize("hasAuthority('database_list')")
    public ResponseEntity listAll(HttpServletRequest request, GeneratorDatabaseQueryCriteria generatorDatabaseQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorDatabaseQueryCriteria.setCreateUserId(userId);
        List<GeneratorDatabaseDTO> generatorDatabaseDTOS;
        if (BeanUtil.isEmpty(generatorDatabaseQueryCriteria)) {
            generatorDatabaseDTOS = generatorDatabaseService.listAll();
        } else {
            generatorDatabaseDTOS = generatorDatabaseService.listAll(generatorDatabaseQueryCriteria.toSpecification());
        }
        List<GeneratorDatabaseVO> convert = Convert.convert(new TypeReference<List<GeneratorDatabaseVO>>() {
        }, generatorDatabaseDTOS);
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/databases/paging")
    @PreAuthorize("hasAuthority('database_list')")
    public ResponseEntity listForPage(HttpServletRequest request, GeneratorDatabaseQueryCriteria generatorDatabaseQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorDatabaseQueryCriteria.setCreateUserId(userId);
        Page<GeneratorDatabase> databases;
        if (BeanUtil.isEmpty(generatorDatabaseQueryCriteria)) {
            databases = generatorDatabaseService.listPageable(pageable);
        } else {
            databases = generatorDatabaseService.listPageable(generatorDatabaseQueryCriteria.toSpecification(), pageable);
        }
        List<GeneratorDatabase> content = databases.getContent();
        List<GeneratorDatabaseVO> convert = Convert.convert(new TypeReference<List<GeneratorDatabaseVO>>() {
        }, content);
        Page<GeneratorDatabaseVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(databases.getNumber(), databases.getSize()), databases.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @PostMapping(value = "/databases")
    @PreAuthorize("hasAuthority('database_add')")
    public ResponseEntity insert(@Validated(GeneratorDatabaseDTO.Insert.class) @RequestBody GeneratorDatabaseDTO generatorDatabaseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorDatabaseService.insert(generatorDatabaseDTO));
    }

    @PutMapping(value = "/databases")
    @PreAuthorize("hasAuthority('database_update')")
    public ResponseEntity put(@Validated(GeneratorDatabaseDTO.Update.class) @RequestBody GeneratorDatabaseDTO generatorDatabaseDTO) {
        generatorDatabaseService.update(generatorDatabaseDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/databases/{id}")
    @PreAuthorize("hasAuthority('database_delete')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        generatorDatabaseService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
