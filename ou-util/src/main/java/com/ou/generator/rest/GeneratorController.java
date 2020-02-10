package com.ou.generator.rest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import com.ou.generator.service.GeneratorService;
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

import com.ou.generator.domain.GeneratorConnection;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.query.GeneratorConnectionQueryCriteria;
import com.ou.generator.domain.vo.GeneratorConnectionVO;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorConnectionService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 *  代码生成
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/util/generate")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @PostMapping("/code")
    // @PreAuthorize("hasAuthority('connection_list')")
    public ResponseEntity generateCode(HttpServletRequest request,
                                       @RequestBody Map<String, Object> map) {
        String projectName = (String) map.get("projectName");
        List<String> tableIds = (List<String>) map.get("tableIds");
        List<Long> collect = tableIds.stream().map(Long::parseLong).collect(Collectors.toList());
        return ResponseEntity.ok(generatorService.generateCode(collect, projectName));
    }

}
