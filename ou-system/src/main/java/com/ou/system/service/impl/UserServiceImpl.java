package com.ou.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.system.domain.User;
import com.ou.system.domain.dto.UserDTO;
import com.ou.system.domain.query.UserQueryCriteria;
import com.ou.system.repository.UserRepository;
import com.ou.system.security.util.SecurityUtil;
import com.ou.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author vince
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private UserRepository userRepository;

    @Override
    public List<UserDTO> listAll() {

        List<User> content = userRepository.findAll();
        log.info("listAll, size: {}", content.size());
        return Convert.convert(new TypeReference<List<UserDTO>>() {
        }, content);
    }

    @Override
    public Page<User> listPageable(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<User> content = users.getContent();
        log.info("listPageable, size: {}", content.size());
        return users;
    }

    @Override
    public Page<User> listPageable(Specification<UserQueryCriteria> specification, Pageable pageable) {
        Page<User> users = userRepository.findAll(specification, pageable);
        List<User> content = users.getContent();
        log.info("listPageable, size: {}", content.size());
        return users;
    }

    @Override
    public UserDTO getById(Long id) {
        User one = userRepository.getOne(id);
        log.info("getById, id: {}, res: {}", id, one.toString());
        return Convert.convert(UserDTO.class, one);
    }

    @Override
    public User getByUsername(String username) {
        User byUsername = userRepository.getByUsername(username);
        log.info("getByUsername, username: {}, res: {}", username, byUsername.toString());
        return byUsername;
    }

    @Override
    public UserDTO insert(UserDTO userDTO) {
        User byUserName = userRepository.getByUsername(userDTO.getUsername());
        if (byUserName != null) {
            log.error("convert insert error: username is exist");
            throw new BadRequestException("用户名已存在");
        }
        User convert = Convert.convert(User.class, userDTO);
        LocalDateTime now = LocalDateTime.now();
        //TODO 加密加盐
        convert.setPassword("123456");
        convert.setCreateTime(now);
        convert.setUpdateTime(now);
        convert.setLastPasswordResetTime(now);
        Long userId = SecurityUtil.getJwtUserId();
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        User save = userRepository.save(convert);
        log.info("insert, res: {}", save.toString());
        return Convert.convert(UserDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            log.error("user delete error: unknown id");
            throw new BadRequestException("未知的id");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Optional<User> byId = userRepository.findById(userDTO.getId());
        if (!byId.isPresent()) {
            log.error("convert update error: unknown id");
            throw new BadRequestException("未知的id");
        }
        if (StrUtil.isNotBlank(userDTO.getUsername())) {
            User byUsername = userRepository.getByUsername(userDTO.getUsername());
            if (byUsername != null && !byUsername.getId().equals(userDTO.getId())) {
                log.error("convert update error: username is exist");
                throw new BadRequestException("用户名已存在");
            }
        }
        User one = byId.get();
        User convert = Convert.convert(User.class, userDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        // 只更新传入的部分字段
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        User save = userRepository.save(one);
        log.info("update, res: {}", save.toString());
        return Convert.convert(UserDTO.class, save);
    }

}
