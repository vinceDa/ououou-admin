package com.ou.generator.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorField;
import com.ou.generator.domain.dto.GeneratorFieldDTO;
import com.ou.generator.domain.query.GeneratorFieldQueryCriteria;
import com.ou.generator.repository.GeneratorFieldRepository;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorFieldService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vince
 * @date 2019/12/6 16:58
 */
@Slf4j
@Service
public class GeneratorFieldServiceImpl implements GeneratorFieldService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GeneratorFieldRepository generatorFieldRepository;

    @Override
    public List<GeneratorFieldDTO> listAll() {
        List<GeneratorField> all = generatorFieldRepository.findAll();
        List<GeneratorFieldDTO> convert = Convert.convert(new TypeReference<List<GeneratorFieldDTO>>() {}, all);
        log.info("listAll, size: {}", convert.size());
        return convert;
    }

    @Override
    public List<GeneratorFieldDTO> listAll(Specification<GeneratorFieldQueryCriteria> specification) {
        List<GeneratorField> all = generatorFieldRepository.findAll(specification);
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<GeneratorFieldDTO>>() {}, all);
    }

    @Override
    public Page<GeneratorField> listPageable(Pageable pageable) {
        Page<GeneratorField> page = generatorFieldRepository.findAll(pageable);
        List<GeneratorField> all = page.getContent();
        log.info("listPageable, size: {}", all.size());
        return page;
    }

    @Override
    public Page<GeneratorField> listPageable(Specification<GeneratorFieldQueryCriteria> specification, Pageable pageable) {
        Page<GeneratorField> page = generatorFieldRepository.findAll(specification, pageable);
        List<GeneratorField> all = page.getContent();
        log.info("listPageableWithSpecification, size: {}", all.size());
        return page;
    }

    @Override
    public GeneratorFieldDTO getById(Long id) {
        GeneratorField one = generatorFieldRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(GeneratorFieldDTO.class, one);
    }

    @Override
    public GeneratorFieldDTO getByName(String name) {
        GeneratorField generatorFieldByName = generatorFieldRepository.getFieldByName(name);
        log.info("getByName, name: {}, connectionByName: {}", name, generatorFieldByName.toString());
        return Convert.convert(GeneratorFieldDTO.class, generatorFieldByName);
    }

    @Override
    public GeneratorFieldDTO insert(GeneratorFieldDTO generatorFieldDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorField generatorFieldByName = generatorFieldRepository.getFieldByName(generatorFieldDTO.getName());
        if (generatorFieldByName != null) {
            log.error("insert error, name is exist");
            throw new BadRequestException("表名已存在");
        }
        GeneratorField convert = Convert.convert(GeneratorField.class, generatorFieldDTO);
        convert.setCreateUserId(userId);
        convert.setCreateUserId(userId);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        GeneratorField save = generatorFieldRepository.save(convert);
        log.info("insert success, info: {}", save.toString());
        return Convert.convert(GeneratorFieldDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (this.existsById(id)) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择表删除");
        }
        generatorFieldRepository.deleteById(id);
    }

    @Override
    public GeneratorFieldDTO update(GeneratorFieldDTO generatorFieldDTO) {
        Optional<GeneratorField> byId = generatorFieldRepository.findById(generatorFieldDTO.getId());
        if (!byId.isPresent()) {
            log.error("update connection error: unknown id");
            throw new BadRequestException("请选择表编辑");
        }
        if (StrUtil.isNotBlank(generatorFieldDTO.getName())) {
            GeneratorField byName = generatorFieldRepository.getFieldByName(generatorFieldDTO.getName());
            if (byName != null && !byName.getId().equals(generatorFieldDTO.getId())) {
                log.error("update connection error: connectionName is exist");
                throw new BadRequestException("表名已存在");
            }
        }
        GeneratorField one = byId.get();
        GeneratorField convert = Convert.convert(GeneratorField.class, generatorFieldDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        GeneratorField save = generatorFieldRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(GeneratorFieldDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return generatorFieldRepository.existsById(id);
    }

    @Override
    public List<GeneratorFieldDTO> listFieldsByTableId(Long tableId) {
        List<GeneratorField> allByTableId = generatorFieldRepository.findAllByTableId(tableId);
        return Convert.convert(new TypeReference<List<GeneratorFieldDTO>>() {
        }, allByTableId);
    }

}
