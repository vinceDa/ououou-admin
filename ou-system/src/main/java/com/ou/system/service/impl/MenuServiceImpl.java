package com.ou.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.Menu;
import com.ou.system.domain.Role;
import com.ou.system.domain.dto.MenuDTO;
import com.ou.system.domain.query.MenuQueryCriteria;
import com.ou.system.repository.MenuRepository;
import com.ou.system.repository.RoleRepository;
import com.ou.system.security.util.SecurityUtil;
import com.ou.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vince
 * @date 2019/10/11 21:54
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    private static final Long ADMIN_ID = 7L;

    @Resource
    private HttpServletRequest request;

    @Resource
    private MenuRepository menuRepository;

    @Resource
    private RoleRepository roleRepository;


    @Override
    public List<MenuDTO> listAll() {
        List<Menu> all = menuRepository.findAll();
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<MenuDTO>>() {
        }, all);
    }

    @Override
    public List<MenuDTO> listAll(Pageable pageable) {
        List<Menu> content = menuRepository.findAll(pageable).getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return Convert.convert(new TypeReference<List<MenuDTO>>() {
        }, content);
    }

    @Override
    public List<MenuDTO> listAll(Specification<MenuQueryCriteria> specification) {
        List all = menuRepository.findAll(specification);
        log.info("listAllWithSpecification, size: {}", all.size());
        return Convert.convert(new TypeReference<List<MenuDTO>>() {}, all);
    }

    @Override
    public List<MenuDTO> listAll(Specification<MenuQueryCriteria> specification, Pageable pageable) {
        List content = menuRepository.findAll(specification, pageable).getContent();
        log.info("listAllWithSpecificationAndPageable, size: {}", content.size());
        return Convert.convert(new TypeReference<List<MenuDTO>>() {
        }, content);
    }

    @Override
    public MenuDTO getById(Long id) {
        Menu one = menuRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(MenuDTO.class, one);
    }

    @Override
    public MenuDTO getByName(String name) {
        Menu byName = menuRepository.getByName(name);
        log.info("getByName, name: {}, byName: {}", name, byName.toString());
        return Convert.convert(MenuDTO.class, byName);
    }

    @Override
    public MenuDTO insert(MenuDTO menuDTO) {
        Menu byName = menuRepository.getByName(menuDTO.getName());
        if (byName != null) {
            log.error("insert convert error: menuName is exist");
            throw new BadRequestException("菜单名已存在");
        }
        Long parentId = menuDTO.getParentId();
        if (parentId != null && !this.existsById(parentId)) {
            log.error("insert convert error: unknown parentId");
            throw new BadRequestException("未知的父级菜单");
        }
        Menu convert = Convert.convert(Menu.class, menuDTO);
        LocalDateTime now = LocalDateTime.now();
        convert.setCreateTime(now);
        convert.setUpdateTime(now);
        Long userId = SecurityUtil.getJwtUserId();
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        Menu save = menuRepository.save(convert);
        log.info("insert, save: {}", save.toString());
        // 新增菜单成功后, 给超级管理员角色自动加上当前菜单
        Role admin = roleRepository.getOne(ADMIN_ID);
        Set<Menu> menus = admin.getMenus();
        if (menus != null && !menus.isEmpty()) {
            log.info("before add operation, admin's menus size: {}", menus.size());
            boolean add = menus.add(save);
            log.info("menu add auto for admin, result: {}", add);
            if (add) {
                log.info("after add operation, admin's menus size: {}", menus.size());
                Role result = roleRepository.save(admin);
                log.info("update admin's menu, menu size: {}", result.getMenus().size());
            }
        }
        return Convert.convert(MenuDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!this.existsById(id)) {
            log.error("delete menu error: unknown id");
            throw new BadRequestException("请选择菜单删除");
        }
        // 删除role_menu的绑定关系
        menuRepository.deleteRoleMenuByMenuId(id);
        menuRepository.deleteById(id);
    }

    @Override
    public MenuDTO update(MenuDTO menuDTO) {
        Optional<Menu> byId = menuRepository.findById(menuDTO.getId());
        if (!byId.isPresent()) {
            log.error("update menu error: unknown id");
            throw new BadRequestException("请选择菜单编辑");
        }
        Long parentId = menuDTO.getParentId();
        if (parentId != null &&  !this.existsById(parentId)) {
            log.error("update menu error: unknown parentId");
            throw new BadRequestException("未知的父级菜单");
        }
        if (StrUtil.isNotBlank(menuDTO.getName())) {
            Menu byName = menuRepository.getByName(menuDTO.getName());
            if (byName != null && !byName.getId().equals(menuDTO.getId())) {
                log.error("update menu error: menuName is exist");
                throw new BadRequestException("菜单名已存在");
            }
        }
        Menu one = byId.get();
        Menu convert = Convert.convert(Menu.class, menuDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        Menu save = menuRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(MenuDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return menuRepository.existsById(id);
    }

    @Override
    public List<MenuDTO> buildMenuTree(List<MenuDTO> originMenuDTOS) {
        // 筛选出所有的父节点
        List<MenuDTO> parents = originMenuDTOS.stream()
                .filter(originMenuDTO -> originMenuDTO.getParentId() == null)
                .sorted(Comparator.comparingInt(MenuDTO::getSort)).collect(Collectors.toList());
        // 存储递归过程中所有的children
        List<MenuDTO> childList = new ArrayList<>();
        recursiveMenuTree(originMenuDTOS, parents, childList);
        originMenuDTOS.removeAll(childList);
        originMenuDTOS.removeAll(parents);
        parents.addAll(originMenuDTOS);
        return parents;
    }

    /**
     *  构建菜单树
     * @param origins 原始数据
     * @param parents 父级菜单
     * @param childList 存储所有的子菜单, 目的是为了避免当搜索到的菜单的父级菜单不被包含在内时, 子菜单被忽略的情况
     */
    private void recursiveMenuTree(List<MenuDTO> origins, List<MenuDTO> parents, List<MenuDTO> childList) {
        for (MenuDTO parent : parents){
            List<MenuDTO> children = origins.stream()
                    .filter(originMenuDTO -> parent.getId().equals(originMenuDTO.getParentId()))
                    .sorted(Comparator.comparingInt(MenuDTO::getSort)).collect(Collectors.toList());
            if (!children.isEmpty()) {
                childList.addAll(children);
                parent.setChildren(children);
                recursiveMenuTree(origins, children, childList);
            }
        }
    }

}
