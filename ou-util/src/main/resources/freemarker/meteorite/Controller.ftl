package ${package}.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ${package}.domain.dto.${entityName}DTO;
import ${package}.domain.query.${entityName}QueryCriteria;
import ${package}.domain.vo.${entityName}VO;
import ${package}.service.${entityName}Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;


/**
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class ${entityName}Controller {

    @Autowired
    private ${entityName}Service ${entityName?uncap_first}Service;

    @GetMapping(value = "/${entityName?uncap_first}s")
    public ResponseEntity listAll(HttpServletRequest request, ${entityName}QueryCriteria ${entityName?uncap_first}QueryCriteria) {
        List<${entityName}DTO> ${entityName?uncap_first}DTOS;
        if (BeanUtil.isEmpty(${entityName?uncap_first}QueryCriteria)) {
            ${entityName?uncap_first}DTOS = ${entityName?uncap_first}Service.listAll();
        } else {
            ${entityName?uncap_first}DTOS = ${entityName?uncap_first}Service.listAll(${entityName?uncap_first}QueryCriteria.toSpecification());
        }
        List<${entityName}VO> convert = Convert.convert(new TypeReference<List<${entityName}VO>>() {
        }, ${entityName?uncap_first}DTOS);
        return ResponseEntity.ok(convert);
    }

    @PostMapping(value = "/${entityName?uncap_first}s")
    public ResponseEntity insert(HttpServletRequest request, @Validated(${entityName}DTO.Insert.class) @RequestBody ${entityName}DTO ${entityName?uncap_first}DTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(${entityName?uncap_first}Service.insert(${entityName?uncap_first}DTO));
    }

    @PutMapping(value = "/${entityName?uncap_first}s")
    public ResponseEntity put(HttpServletRequest request, @Validated(${entityName}DTO.Update.class) @RequestBody ${entityName}DTO ${entityName?uncap_first}DTO) {
        ${entityName?uncap_first}Service.update(${entityName?uncap_first}DTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/${entityName?uncap_first}s/{id}")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        ${entityName?uncap_first}Service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
