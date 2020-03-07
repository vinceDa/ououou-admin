package com.ou.generator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.dto.GeneratorDatabaseDTO;
import com.ou.generator.domain.query.GeneratorDatabaseQueryCriteria;
import com.ou.generator.repository.GeneratorDatabaseRepository;
import com.ou.generator.repository.GeneratorTableRepository;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author vince
 * @date 2019/12/6 16:58
 */
@Slf4j
@Service
public class GeneratorDatabaseServiceImpl implements GeneratorDatabaseService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GeneratorDatabaseRepository generatorDatabaseRepository;

    @Resource
    private GeneratorTableRepository generatorTableRepository;

    @Override
    public List<GeneratorDatabaseDTO> listAll() {
        List<GeneratorDatabase> all = generatorDatabaseRepository.findAll();
        List<GeneratorDatabaseDTO> convert = Convert.convert(new TypeReference<List<GeneratorDatabaseDTO>>() {}, all);
        log.info("listAll, size: {}", convert.size());
        return convert;
    }

    @Override
    public List<GeneratorDatabaseDTO> listAll(Specification<GeneratorDatabaseQueryCriteria> specification) {
        List<GeneratorDatabase> all = generatorDatabaseRepository.findAll(specification);
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<GeneratorDatabaseDTO>>() {}, all);
    }

    @Override
    public Page<GeneratorDatabase> listPageable(Pageable pageable) {
        Page<GeneratorDatabase> page = generatorDatabaseRepository.findAll(pageable);
        List<GeneratorDatabase> all = page.getContent();
        log.info("listPageable, size: {}", all.size());
        return page;
    }

    @Override
    public Page<GeneratorDatabase> listPageable(Specification<GeneratorDatabaseQueryCriteria> specification, Pageable pageable) {
        Page<GeneratorDatabase> page = generatorDatabaseRepository.findAll(specification, pageable);
        List<GeneratorDatabase> all = page.getContent();
        log.info("listPageableWithSpecification, size: {}", all.size());
        return page;
    }

    @Override
    public GeneratorDatabaseDTO getById(Long id) {
        GeneratorDatabase one = generatorDatabaseRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(GeneratorDatabaseDTO.class, one);
    }

    @Override
    public GeneratorDatabaseDTO getByName(String name) {
        GeneratorDatabase generatorDatabaseByName = generatorDatabaseRepository.getDatabaseByName(name);
        log.info("getByName, name: {}, connectionByName: {}", name, generatorDatabaseByName.toString());
        return Convert.convert(GeneratorDatabaseDTO.class, generatorDatabaseByName);
    }

    @Override
    public GeneratorDatabaseDTO insert(GeneratorDatabaseDTO generatorDatabaseDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorDatabase generatorDatabaseByName = generatorDatabaseRepository.getDatabaseByName(generatorDatabaseDTO.getName());
        if (generatorDatabaseByName != null) {
            log.error("insert error, name is exist");
            throw new BadRequestException("数据库名已存在");
        }
        GeneratorDatabase convert = Convert.convert(GeneratorDatabase.class, generatorDatabaseDTO);
        convert.setCreateUserId(userId);
        convert.setCreateUserId(userId);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        GeneratorDatabase save = generatorDatabaseRepository.save(convert);
        log.info("insert success, info: {}", save.toString());
        return Convert.convert(GeneratorDatabaseDTO.class, save);
    }

    @Override
    public void delete(Long id) {
        if (this.existsById(id)) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择数据库删除");
        }
        // 先删除数据库下所有的表
        generatorTableRepository.deleteAllByDatabaseIdIn(Collections.singletonList(id));
        generatorDatabaseRepository.deleteById(id);
    }

    @Override
    public GeneratorDatabaseDTO update(GeneratorDatabaseDTO generatorDatabaseDTO) {
        Optional<GeneratorDatabase> byId = generatorDatabaseRepository.findById(generatorDatabaseDTO.getId());
        if (!byId.isPresent()) {
            log.error("update connection error: unknown id");
            throw new BadRequestException("请选择数据库编辑");
        }
        if (StrUtil.isNotBlank(generatorDatabaseDTO.getName())) {
            GeneratorDatabase byName = generatorDatabaseRepository.getDatabaseByName(generatorDatabaseDTO.getName());
            if (byName != null && !byName.getId().equals(generatorDatabaseDTO.getId())) {
                log.error("update connection error: connectionName is exist");
                throw new BadRequestException("数据库名已存在");
            }
        }
        GeneratorDatabase one = byId.get();
        GeneratorDatabase convert = Convert.convert(GeneratorDatabase.class, generatorDatabaseDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        GeneratorDatabase save = generatorDatabaseRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(GeneratorDatabaseDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return generatorDatabaseRepository.existsById(id);
    }

}
