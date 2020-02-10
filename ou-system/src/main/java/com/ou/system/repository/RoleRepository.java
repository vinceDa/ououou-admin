package com.ou.system.repository;

import com.ou.system.domain.Role;
import com.ou.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @author vince
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {

    /**
     *  根据角色名查询角色信息
     * @param name 角色名
     * @return 角色信息
     */
    Role getByName(String name);

}
