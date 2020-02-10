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

import com.ou.system.domain.Role;
import com.ou.system.domain.dto.RoleDTO;
import com.ou.system.domain.query.RoleQueryCriteria;
import com.ou.system.domain.vo.RoleVO;
import com.ou.system.service.RoleService;

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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/roles/paging")
    // @PreAuthorize("role_list")
    public ResponseEntity listRoleForPage(HttpServletRequest request, RoleQueryCriteria roleQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        roleQueryCriteria.setCreateUserId(userId);
        Page<Role> roles;
        if (BeanUtil.isEmpty(roleQueryCriteria)) {
            roles = roleService.listPageable(pageable);
        } else {
            roles = roleService.listPageable(roleQueryCriteria.toSpecification(), pageable);
        }
        List<Role> content = roles.getContent();
        List<RoleVO> convert = Convert.convert(new TypeReference<List<RoleVO>>() {
        }, content);
        Page<RoleVO> roleVOPage = new PageImpl<>(convert, PageRequest.of(roles.getNumber(), roles.getSize()), roles.getTotalElements());
        return ResponseEntity.ok(roleVOPage);
    }

    @GetMapping(value = "/roles/all")
    public ResponseEntity listAll(HttpServletRequest request, RoleQueryCriteria roleQueryCriteria) {
        Long userId = SecurityUtil.getJwtUserId();
        roleQueryCriteria.setCreateUserId(userId);
        List<RoleDTO> roleDTOS;
        if (BeanUtil.isEmpty(roleQueryCriteria)) {
            roleDTOS = roleService.listAll();
        } else {
            roleDTOS = roleService.listAll(roleQueryCriteria.toSpecification());
        }
        List<RoleVO> convert = Convert.convert(new TypeReference<List<RoleVO>>() {
        }, roleDTOS);
        return ResponseEntity.ok(convert);
    }

    @PostMapping(value = "/roles")
    // @PreAuthorize("role_add")
    public ResponseEntity insert(@Validated(RoleDTO.Insert.class) @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.insert(roleDTO));
    }

    @PutMapping(value = "/roles")
    // @PreAuthorize("role_update")
    public ResponseEntity put(@Validated(RoleDTO.Update.class) @RequestBody RoleDTO roleDTO) {
        roleService.update(roleDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/roles/{id}")
    // @PreAuthorize("role_delete")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        roleService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/roles/menus")
    public ResponseEntity updateRoleMenus(@Validated(RoleDTO.Update.class) @RequestBody RoleDTO roleDTO) {
        roleService.updateRoleMenus(roleDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
