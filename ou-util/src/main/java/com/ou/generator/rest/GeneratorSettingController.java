package com.ou.generator.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.ou.generator.domain.vo.GeneratorSettingVO;
import com.ou.generator.service.GeneratorSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.generator.domain.dto.GeneratorSettingDTO;
import com.ou.generator.domain.vo.GeneratorFieldVO;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/util/generate")
public class GeneratorSettingController {

    @Autowired
    private GeneratorSettingService generatorSettingService;


    @PostMapping(value = "/settings")
    public ResponseEntity
        insert(@Validated(GeneratorSettingDTO.Insert.class) @RequestBody GeneratorSettingDTO generatorSettingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorSettingService.insert(generatorSettingDTO));
    }

    @PutMapping(value = "/settings")
    public ResponseEntity put(@RequestBody GeneratorSettingDTO generatorSettingDTO) {
        generatorSettingService.update(generatorSettingDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/settings/{id}")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        generatorSettingService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/table/{tableId}/settings")
    public ResponseEntity listSettingsByTableId(@NotNull @PathVariable Long tableId) {
        return ResponseEntity.ok(Convert.convert(new TypeReference<List<GeneratorSettingVO>>() {},
                generatorSettingService.listSettingsByTableId(tableId)));
    }

}
