package com.ou.generator.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ou.generator.service.GeneratorTableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorTable;
import com.ou.generator.domain.dto.GeneratorTableDTO;
import com.ou.generator.domain.query.GeneratorTableQueryCriteria;
import com.ou.generator.repository.GeneratorTableRepository;
import com.ou.generator.security.util.SecurityUtil;

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
public class GeneratorTableServiceImpl implements GeneratorTableService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GeneratorTableRepository generatorTableRepository;

    @Override
    public List<GeneratorTableDTO> listAll() {
        List<GeneratorTable> all = generatorTableRepository.findAll();
        List<GeneratorTableDTO> convert = Convert.convert(new TypeReference<List<GeneratorTableDTO>>() {}, all);
        log.info("listAll, size: {}", convert.size());
        return convert;
    }

    @Override
    public List<GeneratorTableDTO> listAll(Specification<GeneratorTableQueryCriteria> specification) {
        List<GeneratorTable> all = generatorTableRepository.findAll(specification);
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<GeneratorTableDTO>>() {}, all);
    }

    @Override
    public Page<GeneratorTable> listPageable(Pageable pageable) {
        Page<GeneratorTable> page = generatorTableRepository.findAll(pageable);
        List<GeneratorTable> all = page.getContent();
        log.info("listPageable, size: {}", all.size());
        return page;
    }

    @Override
    public Page<GeneratorTable> listPageable(Specification<GeneratorTableQueryCriteria> specification, Pageable pageable) {
        Page<GeneratorTable> page = generatorTableRepository.findAll(specification, pageable);
        List<GeneratorTable> all = page.getContent();
        log.info("listPageableWithSpecification, size: {}", all.size());
        return page;
    }

    @Override
    public GeneratorTableDTO getById(Long id) {
        GeneratorTable one = generatorTableRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(GeneratorTableDTO.class, one);
    }

    @Override
    public GeneratorTableDTO getByName(String name) {
        GeneratorTable generatorTableByName = generatorTableRepository.getTableByName(name);
        log.info("getByName, name: {}, connectionByName: {}", name, generatorTableByName.toString());
        return Convert.convert(GeneratorTableDTO.class, generatorTableByName);
    }

    @Override
    public GeneratorTableDTO insert(GeneratorTableDTO generatorTableDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorTable generatorTableByName = generatorTableRepository.getTableByName(generatorTableDTO.getName());
        if (generatorTableByName != null) {
            log.error("insert error, name is exist");
            throw new BadRequestException("表名已存在");
        }
        GeneratorTable convert = Convert.convert(GeneratorTable.class, generatorTableDTO);
        convert.setCreateUserId(userId);
        convert.setCreateUserId(userId);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        GeneratorTable save = generatorTableRepository.save(convert);
        log.info("insert success, info: {}", save.toString());
        return Convert.convert(GeneratorTableDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (this.existsById(id)) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择表删除");
        }
        generatorTableRepository.deleteById(id);
    }

    @Override
    public GeneratorTableDTO update(GeneratorTableDTO generatorTableDTO) {
        Optional<GeneratorTable> byId = generatorTableRepository.findById(generatorTableDTO.getId());
        if (!byId.isPresent()) {
            log.error("update connection error: unknown id");
            throw new BadRequestException("请选择表编辑");
        }
        if (StrUtil.isNotBlank(generatorTableDTO.getName())) {
            GeneratorTable byName = generatorTableRepository.getTableByName(generatorTableDTO.getName());
            if (byName != null && !byName.getId().equals(generatorTableDTO.getId())) {
                log.error("update connection error: connectionName is exist");
                throw new BadRequestException("表名已存在");
            }
        }
        GeneratorTable one = byId.get();
        GeneratorTable convert = Convert.convert(GeneratorTable.class, generatorTableDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        GeneratorTable save = generatorTableRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(GeneratorTableDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return generatorTableRepository.existsById(id);
    }

}
