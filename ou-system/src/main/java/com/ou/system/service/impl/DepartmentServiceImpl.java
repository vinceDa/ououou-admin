package com.ou.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.Department;
import com.ou.system.domain.dto.DepartmentDTO;
import com.ou.system.domain.query.DepartmentQueryCriteria;
import com.ou.system.repository.DepartmentRepository;
import com.ou.system.security.util.SecurityUtil;
import com.ou.system.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author vince
 * @date 2019/10/11 23:38
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDTO> listAll() {
        List<Department> all = departmentRepository.findAll();
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<DepartmentDTO>>() {}, all);
    }

    @Override
    public List<DepartmentDTO> listAll(Specification<DepartmentQueryCriteria> specification) {
        List<DepartmentDTO> all = departmentRepository.findAll(specification);
        log.info("listAllWithSpecification, size: {}", all.size());
        return Convert.convert(new TypeReference<List<DepartmentDTO>>() {}, all);
    }

    @Override
    public Page<Department> listPageable(Pageable pageable) {
        Page<Department> page = departmentRepository.findAll(pageable);
        List<Department> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public Page<Department> listPageable(Specification<DepartmentQueryCriteria> specification, Pageable pageable) {
        Page<Department> page = departmentRepository.findAll(specification, pageable);
        List<Department> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public DepartmentDTO getById(Long id) {
        Department one = departmentRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(DepartmentDTO.class, one);
    }

    @Override
    public DepartmentDTO getByName(String name) {
        Department byName = departmentRepository.getByName(name);
        log.info("getByName, name: {}, byName: {}", name, byName.toString());
        return Convert.convert(DepartmentDTO.class, byName);
    }

    @Override
    public DepartmentDTO insert(DepartmentDTO departmentDTO) {
        Department byName = departmentRepository.getByName(departmentDTO.getName());
        if (byName != null) {
            log.error("insert department error: departmentName is exist");
            throw new BadRequestException("部门名已存在");
        }
        Long parentId = departmentDTO.getParentId();
        if (parentId != null && !this.existsById(parentId)) {
            log.error("insert department error: unknown parentId");
            throw new BadRequestException("未知的父级部门");
        }
        Department convert = Convert.convert(Department.class, departmentDTO);
        LocalDateTime now = LocalDateTime.now();
        convert.setCreateTime(now);
        convert.setUpdateTime(now);
        Long userId = SecurityUtil.getJwtUserId();
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        Department save = departmentRepository.save(convert);
        log.info("insert, save: {}", save.toString());
        return Convert.convert(DepartmentDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!this.existsById(id)) {
            log.error("delete department error: unknown id");
            throw new BadRequestException("请选择部门删除");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        Optional<Department> byId = departmentRepository.findById(departmentDTO.getId());
        if (!byId.isPresent()) {
            log.error("update department error: unknown id");
            throw new BadRequestException("请选择部门编辑");
        }
        Long parentId = departmentDTO.getParentId();
        if (parentId != null && !this.existsById(parentId)) {
            log.error("update department error: unknown parentId");
            throw new BadRequestException("未知的父级部门");
        }
        if (StrUtil.isNotBlank(departmentDTO.getName())) {
            Department byName = departmentRepository.getByName(departmentDTO.getName());
            if (byName != null && !byName.getId().equals(departmentDTO.getId())) {
                log.error("update department error: departmentName is exist");
                throw new BadRequestException("部门名已存在");
            }
        }
        Department one = byId.get();
        Department convert = Convert.convert(Department.class, departmentDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        Department save = departmentRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(DepartmentDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public List<DepartmentDTO> buildMenuTree(List<DepartmentDTO> departmentDTOS) {
        List<DepartmentDTO> parents = departmentDTOS.stream()
                .filter(departmentDTO -> departmentDTO.getParentId() == null).collect(Collectors.toList());
        List<DepartmentDTO> childList = new ArrayList<>();
        recursiveDepartmentTree(departmentDTOS, parents, childList);
        departmentDTOS.removeAll(childList);
        departmentDTOS.removeAll(parents);
        parents.addAll(departmentDTOS);
        return parents;
    }

    /**
     *  构建部门树
     * @param origins 原始数据
     * @param parents 父级部门
     * @param childList 存储所有的子部门, 目的是为了避免当搜索到的部门的父级部门不被包含在内时, 子部门被忽略的情况
     */
    private void recursiveDepartmentTree(List<DepartmentDTO> origins, List<DepartmentDTO> parents, List<DepartmentDTO> childList) {
        for (DepartmentDTO parent : parents) {
            List<DepartmentDTO> children = origins.stream()
                    .filter(departmentDTO -> parent.getId().equals(departmentDTO.getParentId())).collect(Collectors.toList());
            if (!children.isEmpty()) {
                parent.setChildren(children);
                childList.addAll(children);
                recursiveDepartmentTree(origins, children, childList);
            }

        }
    }

}
