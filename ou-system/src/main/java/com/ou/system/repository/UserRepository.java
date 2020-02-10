package com.ou.system.repository;

import com.ou.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author vince
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    /**
     *  根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     *  列出所有的用户信息
     * @return 所有的用户的集合
     */
    @Query(value = "select * from system_user",nativeQuery = true)
    List<User> listAll();

    /**
     *  列出当前登陆用户下的用户列表
     * @param createUserId 当前登录的用户id
     * @return 用户集合
     */
    List<User> findAllByCreateUserId(String createUserId);
}
