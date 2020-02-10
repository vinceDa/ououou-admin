package com.ou.system.service;

import com.ou.system.domain.User;
import com.ou.system.domain.dto.UserDTO;
import com.ou.system.domain.query.UserQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author vince
 */
public interface UserService {

    /**
     *  返回所有的用户信息
     * @return  返回所有的用户信息
     */
    List<UserDTO> listAll();

    /**
     *  分页查询用户信息
     * @param pageable 分页参数实体类
     * @return 符合条件的用户信息集合
     */
    Page<User> listPageable(Pageable pageable);

    /**
     *  分页查询用户信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的用户信息集合
     */
    Page<User> listPageable(Specification<UserQueryCriteria> specification, Pageable pageable);

    /**
     *  根据用户id获取用户信息
     * @param id  用户id
     * @return 传入id对应的用户信息
     */
    UserDTO getById(Long id);

    /**
     *  根据用户名称获取对应的用户信息
     * @param username  用户名
     * @return 传入的用户名所对应的用户信息
     */
    User getByUsername(String username);

    /**
     *  新增用户
     * @param userDTO 用户信息实体
     * @return 新增成功实体类
     */
    UserDTO insert(UserDTO userDTO);

    /**
     *  删除用户
     * @param id 用户id
     */
    void delete(Long id);

    /**
     *  更新用户
     * @param userDTO 需要更新的用户信息
     * @return 更新成功的实体类
     */
    UserDTO update(UserDTO userDTO);
}
