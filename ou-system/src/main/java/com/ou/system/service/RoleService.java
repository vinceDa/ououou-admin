package com.ou.system.service;

import com.ou.system.domain.Role;
import com.ou.system.domain.dto.RoleDTO;
import com.ou.system.domain.query.RoleQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author vince
 */
public interface RoleService {

    /**
     *  返回所有的角色信息
     * @return  返回所有的角色信息
     */
    List<RoleDTO> listAll();

    /**
     *  根据条件查询角色信息
     * @param specification 查询条件实体
     * @return 符合条件的角色信息集合
     */
    List<RoleDTO> listAll(Specification specification);

    /**
     *  分页查询角色信息
     * @param pageable 分页参数实体类
     * @return 符合条件的角色信息集合
     */
    Page<Role> listPageable(Pageable pageable);

    /**
     *  分页查询角色信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的角色信息集合
     */
    Page<Role> listPageable(Specification<RoleQueryCriteria> specification, Pageable pageable);

    /**
     *  根据角色id获取角色信息
     * @param id  角色id
     * @return 传入id对应的角色信息
     */
    RoleDTO getById(Long id);

    /**
     *  根据角色名称获取对应的角色信息
     * @param name  角色名
     * @return 传入的角色名所对应的角色信息
     */
    RoleDTO getByName(String name);

    /**
     *  新增角色
     * @param roleDTO 角色信息实体
     * @return 新增成功实体类
     */
    RoleDTO insert(RoleDTO roleDTO);

    /**
     *  删除角色
     * @param id 角色id
     */
    void delete(Long id);

    /**
     *  更新角色
     * @param roleDTO 需要更新的角色信息
     * @return 更新成功的实体类
     */
    RoleDTO update(RoleDTO roleDTO);

    /**
     *  更新角色的菜单
     * @param roleDTO  需要更新的角色信息
     * @return 更新成功的实体类
     */
    RoleDTO updateRoleMenus(RoleDTO roleDTO);
}
