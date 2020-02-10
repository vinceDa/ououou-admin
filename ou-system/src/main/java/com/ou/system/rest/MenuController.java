package com.ou.system.rest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import com.ou.system.security.domain.JwtUser;
import com.ou.system.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.system.domain.Menu;
import com.ou.system.domain.Role;
import com.ou.system.domain.dto.MenuDTO;
import com.ou.system.domain.dto.UserDTO;
import com.ou.system.domain.query.MenuQueryCriteria;
import com.ou.system.domain.vo.MenuVO;
import com.ou.system.service.MenuService;
import com.ou.system.service.UserService;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/system")
public class MenuController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/menus")
    public ResponseEntity listAll(HttpServletRequest request, MenuQueryCriteria menuQueryCriteria) {
        Long userId = SecurityUtil.getJwtUserId();
        menuQueryCriteria.setCreateUserId(userId);
        UserDTO byId = userService.getById(userId);
        Set<Role> roles = byId.getRoles();
        Iterator<Role> iterator = roles.iterator();
        Set<Menu> totalMenu = new HashSet<>();
        while (iterator.hasNext()) {
            Role next = iterator.next();
            totalMenu.addAll(next.getMenus());
        }
        List<MenuDTO> menuDTOS = Convert.convert(new TypeReference<List<MenuDTO>>() {
        }, totalMenu);
        List<MenuVO> convert = Convert.convert(new TypeReference<List<MenuVO>>() {
        }, menuService.buildMenuTree(menuDTOS));
        return ResponseEntity.ok(convert);
    }

    @GetMapping(value = "/menus/tree")
    public ResponseEntity buildMenuTree(HttpServletRequest request, MenuQueryCriteria menuQueryCriteria) {
        Long userId = SecurityUtil.getJwtUserId();
        menuQueryCriteria.setCreateUserId(userId);
        UserDTO byId = userService.getById(userId);
        Set<Role> roles = byId.getRoles();
        Iterator<Role> iterator = roles.iterator();
        Set<Menu> totalMenu = new HashSet<>();
        while (iterator.hasNext()) {
            Role next = iterator.next();
            totalMenu.addAll(next.getMenus());
        }
        List<MenuDTO> menuDTOS = Convert.convert(new TypeReference<List<MenuDTO>>() {
        }, totalMenu);
        // 过滤掉所有按钮级别的菜单
        menuDTOS = menuDTOS.stream().filter(single -> !"2".equals(single.getType())).collect(Collectors.toList());
        List<MenuVO> convert = Convert.convert(new TypeReference<List<MenuVO>>() {
        }, menuService.buildMenuTree(menuDTOS));
        return ResponseEntity.ok(convert);
    }

    @PostMapping(value = "/menus")
    @PreAuthorize("hasAuthority('menu_add')")
    public ResponseEntity insert(@Validated(MenuDTO.Insert.class) @RequestBody MenuDTO menuDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.insert(menuDTO));
    }

    @PutMapping(value = "/menus")
    // @PreAuthorize("menu_update")
    public ResponseEntity put(@Validated(MenuDTO.Update.class) @RequestBody MenuDTO menuDTO) {
        menuService.update(menuDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/menus/{id}")
    // @PreAuthorize("menu_delete")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        menuService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}

