package com.ou.system.repository;

import com.ou.system.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vince
 */
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor {

    /**
     *  根据菜单名查询菜单信息
     * @param name 菜单名
     * @return 菜单信息
     */
    Menu getByName(String name);

    /**
     *  删除菜单和角色的绑定关系
     * @param menuId 菜单id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "delete from system_role_menu where menu_id = ?1", nativeQuery = true)
    void deleteRoleMenuByMenuId(Long menuId);

}
