package com.ou.generator.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.ou.generator.domain.GeneratorConnection;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.query.GeneratorConnectionQueryCriteria;
import com.ou.generator.domain.vo.GeneratorConnectionVO;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorConnectionService;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/util/generate")
public class GeneratorConnectionController {

    @Autowired
    private GeneratorConnectionService generatorConnectionService;

    @GetMapping(value = "/connections")
    @PreAuthorize("hasAuthority('connection_list')")
    public ResponseEntity listAll(HttpServletRequest request, GeneratorConnectionQueryCriteria generatorConnectionQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorConnectionQueryCriteria.setCreateUserId(userId);
        List<GeneratorConnectionDTO> generatorConnectionDTOS;
        if (BeanUtil.isEmpty(generatorConnectionQueryCriteria)) {
            generatorConnectionDTOS = generatorConnectionService.listAll();
        } else {
            generatorConnectionDTOS = generatorConnectionService.listAll(generatorConnectionQueryCriteria.toSpecification());
        }
        List<GeneratorConnectionVO> convert = Convert.convert(new TypeReference<List<GeneratorConnectionVO>>() {
        }, generatorConnectionDTOS);
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/connections/paging")
    @PreAuthorize("hasAuthority('connection_list')")
    public ResponseEntity listForPage(HttpServletRequest request, GeneratorConnectionQueryCriteria generatorConnectionQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorConnectionQueryCriteria.setCreateUserId(userId);
        Page<GeneratorConnection> connections;
        if (BeanUtil.isEmpty(generatorConnectionQueryCriteria)) {
            connections = generatorConnectionService.listPageable(pageable);
        } else {
            connections = generatorConnectionService.listPageable(generatorConnectionQueryCriteria.toSpecification(), pageable);
        }
        List<GeneratorConnection> content = connections.getContent();
        List<GeneratorConnectionVO> convert = Convert.convert(new TypeReference<List<GeneratorConnectionVO>>() {
        }, content);
        Page<GeneratorConnectionVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(connections.getNumber(), connections.getSize()), connections.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @PostMapping(value = "/connections")
    @PreAuthorize("hasAuthority('connection_add')")
    public ResponseEntity insert(@Validated(GeneratorConnectionDTO.Insert.class) @RequestBody GeneratorConnectionDTO generatorConnectionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorConnectionService.insert(generatorConnectionDTO));
    }

    @PutMapping(value = "/connections")
    @PreAuthorize("hasAuthority('connection_update')")
    public ResponseEntity update(@Validated(GeneratorConnectionDTO.Update.class) @RequestBody GeneratorConnectionDTO generatorConnectionDTO) {
        generatorConnectionService.update(generatorConnectionDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/connections/{id}")
    @PreAuthorize("hasAuthority('connection_delete')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        generatorConnectionService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/connections/test/{id}")
    // @PreAuthorize("hasAuthority('connection_test')")
    public ResponseEntity testConnection(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(generatorConnectionService.testConnection(id));
    }

    @PostMapping(value = "/connections/refresh/{id}")
    // @PreAuthorize("hasAuthority('connection_refresh')")
    public ResponseEntity refresh(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generatorConnectionService.refresh(id));
    }

    @GetMapping(value = "/connections/infoTree")
    // @PreAuthorize("hasAuthority('connection_test')")
    public ResponseEntity listConnectionInfoTree() {
        return ResponseEntity.ok(generatorConnectionService.listConnectionInfoTree());
    }
}
