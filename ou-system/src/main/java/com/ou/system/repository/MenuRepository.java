package com.ou.system.repository;

import com.ou.system.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

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

}
