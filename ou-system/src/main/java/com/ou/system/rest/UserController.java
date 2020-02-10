package com.ou.system.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.ou.system.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.system.domain.User;
import com.ou.system.domain.dto.UserDTO;
import com.ou.system.domain.query.UserQueryCriteria;
import com.ou.system.domain.vo.UserVO;
import com.ou.system.service.DepartmentService;
import com.ou.system.service.JobService;
import com.ou.system.service.UserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;


/**
 * @author vince
 */
@Controller
@RequestMapping("api/v1/system")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JobService jobService;

    @GetMapping(value = "/users/paging")
    @PreAuthorize("hasAuthority('user_list')")
    public ResponseEntity listForPage(HttpServletRequest request, UserQueryCriteria userQueryCriteria, Pageable pageable) {
        Long userId = SecurityUtil.getJwtUserId();
        userQueryCriteria.setCreateUserId(userId);
        boolean empty = BeanUtil.isEmpty(userQueryCriteria);
        Page<User> users;
        if (empty) {
            users = userService.listPageable(pageable);
        } else {
            users = userService.listPageable(userQueryCriteria.toSpecification(), pageable);
        }
        List<User> content = users.getContent();
        List<UserVO> convert = Convert.convert(new TypeReference<List<UserVO>>() {
        }, content);
        for (UserVO single : convert) {
            if (single.getDepartmentId() != null) {
                single.setDepartmentName(departmentService.getById(single.getDepartmentId()).getName());
            }
            if (single.getJobId() != null) {
                single.setJobName(jobService.getById(single.getJobId()).getName());
            }
        }
        Page<UserVO> userVOPage = new PageImpl<>(convert, PageRequest.of(users.getNumber(), users.getSize()), users.getTotalElements());
        return ResponseEntity.ok(userVOPage);
    }

    @PostMapping(value = "/users")
    @PreAuthorize("hasAuthority('user_add')")
    public ResponseEntity insert(@Validated(UserDTO.Insert.class) @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insert(userDTO));
    }

    @PutMapping(value = "/users")
    @PreAuthorize("hasAuthority('user_update')")
    public ResponseEntity put(@Validated(UserDTO.Update.class) @RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAuthority('user_delete')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/users/getByUsername")
    @ResponseBody
    public User getByUsername(@RequestParam(value = "username") String username, HttpServletResponse response) {
        return userService.getByUsername(username);
    }

}
