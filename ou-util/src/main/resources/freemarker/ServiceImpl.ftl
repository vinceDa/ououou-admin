package ${package}.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ${package}.domain.${entityName};
import ${package}.domain.dto.${entityName}DTO;
import ${package}.domain.query.${entityName}QueryCriteria;
import ${package}.repository.${entityName}Repository;
import ${package}.service.${entityName}Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Service
public class ${entityName}ServiceImpl implements ${entityName}Service {

    @Resource
    private ${entityName}Repository ${entityName?uncap_first}Repository;

    @Override
    public List<${entityName}DTO> listAll() {
        List<${entityName}> all = ${entityName?uncap_first}Repository.findAll();
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<${entityName}DTO>>() {}, all);
    }

    @Override
    public List<${entityName}DTO> listAll(Specification<${entityName}QueryCriteria> specification) {
        List<${entityName}DTO> all = ${entityName?uncap_first}Repository.findAll(specification);
        log.info("listAllWithSpecification, size: {}", all.size());
        return Convert.convert(new TypeReference<List<${entityName}DTO>>() {}, all);
    }

    @Override
    public Page<${entityName}> listPageable(Pageable pageable) {
        Page<${entityName}> page = ${entityName?uncap_first}Repository.findAll(pageable);
        List<${entityName}> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public Page<${entityName}> listPageable(Specification<${entityName}QueryCriteria> specification, Pageable pageable) {
        Page<${entityName}> page = ${entityName?uncap_first}Repository.findAll(specification, pageable);
        List<${entityName}> content = page.getContent();
        log.info("listAllWithPageable, size: {}", content.size());
        return page;
    }

    @Override
    public ${entityName}DTO getById(Long id) {
        ${entityName} one = ${entityName?uncap_first}Repository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(${entityName}DTO.class, one);
    }

    @Override
    public ${entityName}DTO insert(${entityName}DTO ${entityName?uncap_first}DTO) {
        ${entityName} convert = Convert.convert(${entityName}.class, ${entityName?uncap_first}DTO);
        ${entityName} save = ${entityName?uncap_first}Repository.save(convert);
        log.info("insert, save: {}", save.toString());
        return Convert.convert(${entityName}DTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (!${entityName?uncap_first}Repository.existsById(id)) {
            log.error("delete ${entityName?uncap_first} error: unknown id");
        }
        ${entityName?uncap_first}Repository.deleteById(id);
    }

    @Override
    public ${entityName}DTO update(${entityName}DTO ${entityName?uncap_first}DTO) {
        Optional<${entityName}> byId = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}DTO.getId());
        if (!byId.isPresent()) {
            log.error("update ${entityName?uncap_first} error: unknown id");
        }
        ${entityName} one = byId.get();
        ${entityName} convert = Convert.convert(${entityName}.class, ${entityName?uncap_first}DTO);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        ${entityName} save = ${entityName?uncap_first}Repository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(${entityName}DTO.class, save);
    }

}
