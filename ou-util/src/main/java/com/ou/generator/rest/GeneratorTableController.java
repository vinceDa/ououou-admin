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

import com.ou.generator.domain.GeneratorTable;
import com.ou.generator.domain.dto.GeneratorTableDTO;
import com.ou.generator.domain.query.GeneratorTableQueryCriteria;
import com.ou.generator.domain.vo.GeneratorTableVO;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorTableService;

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
public class GeneratorTableController {

    @Autowired
    private GeneratorTableService generatorTableService;

    @GetMapping(value = "/tables")
    @PreAuthorize("hasAuthority('table_list')")
    public ResponseEntity listAll(HttpServletRequest request, GeneratorTableQueryCriteria generatorTableQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorTableQueryCriteria.setCreateUserId(userId);
        List<GeneratorTableDTO> generatorTableDTOS;
        if (BeanUtil.isEmpty(generatorTableQueryCriteria)) {
            generatorTableDTOS = generatorTableService.listAll();
        } else {
            generatorTableDTOS = generatorTableService.listAll(generatorTableQueryCriteria.toSpecification());
        }
        List<GeneratorTableVO> convert = Convert.convert(new TypeReference<List<GeneratorTableVO>>() {
        }, generatorTableDTOS);
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/tables/paging")
    @PreAuthorize("hasAuthority('table_list')")
    public ResponseEntity listForPage(HttpServletRequest request, GeneratorTableQueryCriteria generatorTableQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorTableQueryCriteria.setCreateUserId(userId);
        Page<GeneratorTable> tables;
        if (BeanUtil.isEmpty(generatorTableQueryCriteria)) {
            tables = generatorTableService.listPageable(pageable);
        } else {
            tables = generatorTableService.listPageable(generatorTableQueryCriteria.toSpecification(), pageable);
        }
        List<GeneratorTable> content = tables.getContent();
        List<GeneratorTableVO> convert = Convert.convert(new TypeReference<List<GeneratorTableVO>>() {
        }, content);
        Page<GeneratorTableVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(tables.getNumber(), tables.getSize()), tables.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @PostMapping(value = "/tables")
    @PreAuthorize("hasAuthority('table_add')")
    public ResponseEntity insert(@Validated(GeneratorTableDTO.Insert.class) @RequestBody GeneratorTableDTO generatorTableDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorTableService.insert(generatorTableDTO));
    }

    @PutMapping(value = "/tables")
    @PreAuthorize("hasAuthority('table_update')")
    public ResponseEntity put(@Validated(GeneratorTableDTO.Update.class) @RequestBody GeneratorTableDTO generatorTableDTO) {
        generatorTableService.update(generatorTableDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/tables/{id}")
    @PreAuthorize("hasAuthority('table_delete')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        generatorTableService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
