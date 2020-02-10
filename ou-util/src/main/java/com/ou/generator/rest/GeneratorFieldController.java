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

import com.ou.generator.domain.GeneratorField;
import com.ou.generator.domain.dto.GeneratorFieldDTO;
import com.ou.generator.domain.query.GeneratorFieldQueryCriteria;
import com.ou.generator.domain.vo.GeneratorFieldVO;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorFieldService;

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
public class GeneratorFieldController {

    @Autowired
    private GeneratorFieldService generatorFieldService;

    @GetMapping(value = "/fields")
    @PreAuthorize("hasAuthority('field_list')")
    public ResponseEntity listAll(HttpServletRequest request, GeneratorFieldQueryCriteria generatorFieldQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorFieldQueryCriteria.setCreateUserId(userId);
        List<GeneratorFieldDTO> generatorFieldDTOS;
        if (BeanUtil.isEmpty(generatorFieldQueryCriteria)) {
            generatorFieldDTOS = generatorFieldService.listAll();
        } else {
            generatorFieldDTOS = generatorFieldService.listAll(generatorFieldQueryCriteria.toSpecification());
        }
        List<GeneratorFieldVO> convert = Convert.convert(new TypeReference<List<GeneratorFieldVO>>() {
        }, generatorFieldDTOS);
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/fields/paging")
    @PreAuthorize("hasAuthority('field_list')")
    public ResponseEntity listForPage(HttpServletRequest request, GeneratorFieldQueryCriteria generatorFieldQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorFieldQueryCriteria.setCreateUserId(userId);
        Page<GeneratorField> fields;
        if (BeanUtil.isEmpty(generatorFieldQueryCriteria)) {
            fields = generatorFieldService.listPageable(pageable);
        } else {
            fields = generatorFieldService.listPageable(generatorFieldQueryCriteria.toSpecification(), pageable);
        }
        List<GeneratorField> content = fields.getContent();
        List<GeneratorFieldVO> convert = Convert.convert(new TypeReference<List<GeneratorFieldVO>>() {
        }, content);
        Page<GeneratorFieldVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(fields.getNumber(), fields.getSize()), fields.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @PostMapping(value = "/fields")
    @PreAuthorize("hasAuthority('field_add')")
    public ResponseEntity insert(@Validated(GeneratorFieldDTO.Insert.class) @RequestBody GeneratorFieldDTO generatorFieldDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorFieldService.insert(generatorFieldDTO));
    }

    @PutMapping(value = "/fields")
    @PreAuthorize("hasAuthority('field_update')")
    public ResponseEntity put(@Validated(GeneratorFieldDTO.Update.class) @RequestBody GeneratorFieldDTO generatorFieldDTO) {
        generatorFieldService.update(generatorFieldDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/fields/{id}")
    @PreAuthorize("hasAuthority('field_delete')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        generatorFieldService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/table/{tableId}/fields")
    // @PreAuthorize("hasAuthority('field_delete')")
    public ResponseEntity listFieldsByTableId(@NotNull @PathVariable Long tableId) {
        List<GeneratorFieldDTO> generatorFieldDTOS = generatorFieldService.listFieldsByTableId(tableId);
        return ResponseEntity.ok(Convert.convert(new TypeReference<List<GeneratorFieldVO>>() {
        }, generatorFieldDTOS));
    }

}
