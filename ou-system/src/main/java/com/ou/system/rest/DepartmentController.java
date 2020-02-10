package com.ou.system.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import com.ou.system.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ou.system.domain.dto.DepartmentDTO;
import com.ou.system.domain.query.DepartmentQueryCriteria;
import com.ou.system.domain.vo.DepartmentVO;
import com.ou.system.service.DepartmentService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/system")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/departments")
    public ResponseEntity listAll(HttpServletRequest request, DepartmentQueryCriteria departmentQueryCriteria) {
        Long userId = SecurityUtil.getJwtUserId();
        departmentQueryCriteria.setCreateUserId(userId);
        List<DepartmentDTO> departmentDTOS;
        if (BeanUtil.isEmpty(departmentQueryCriteria)) {
            departmentDTOS = departmentService.listAll();
        } else {
            departmentDTOS = departmentService.listAll(departmentQueryCriteria.toSpecification());
        }
        List<DepartmentVO> convert = Convert.convert(new TypeReference<List<DepartmentVO>>() {
        }, departmentService.buildMenuTree(departmentDTOS));
        return ResponseEntity.ok(convert);
    }

    @PostMapping(value = "/departments")
    // @PreAuthorize("department_add")
    public ResponseEntity insert(HttpServletRequest request, @Validated(DepartmentDTO.Insert.class) @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.insert(departmentDTO));
    }

    @PutMapping(value = "/departments")
    // @PreAuthorize("department_update")
    public ResponseEntity put(HttpServletRequest request, @Validated(DepartmentDTO.Update.class) @RequestBody DepartmentDTO departmentDTO) {
        departmentService.update(departmentDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/departments/{id}")
    // @PreAuthorize("department_delete")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        departmentService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
