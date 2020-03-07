package com.ou.generator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorSetting;
import com.ou.generator.domain.dto.GeneratorSettingDTO;
import com.ou.generator.repository.GeneratorSettingRepository;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author vince
 * @date 2020/1/9 14:29
 */
@Slf4j
@Service
public class GeneratorSettingServiceImpl implements GeneratorSettingService {

    @Resource
    private GeneratorSettingRepository generatorSettingRepository;

    @Override
    public GeneratorSettingDTO getById(Long id) {
        return Convert.convert(GeneratorSettingDTO.class, generatorSettingRepository.getOne(id));
    }

    @Override
    public GeneratorSettingDTO insert(GeneratorSettingDTO generatorSettingDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorSetting convert = Convert.convert(GeneratorSetting.class, generatorSettingDTO);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        return Convert.convert(GeneratorSettingDTO.class, generatorSettingRepository.save(convert));
    }

    @Override
    public void delete(Long id) {
        if (!this.existsById(id)) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择配置进行删除");
        }
        generatorSettingRepository.deleteById(id);
    }

    @Override
    public GeneratorSettingDTO update(GeneratorSettingDTO generatorSettingDTO) {
        Optional<GeneratorSetting> byId = generatorSettingRepository.findById(generatorSettingDTO.getId());
        if (!byId.isPresent()) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择配置进行编辑");
        }
        GeneratorSetting one = byId.get();
        GeneratorSetting convert = Convert.convert(GeneratorSetting.class, generatorSettingDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        GeneratorSetting save = generatorSettingRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(GeneratorSettingDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return generatorSettingRepository.existsById(id);
    }

    @Override
    public GeneratorSettingDTO listSettingsByTableId(Long tableId) {
        GeneratorSetting byTableId = generatorSettingRepository.getByTableId(tableId);
        log.info("listSettingsByTableId: {}", byTableId);
        return Convert.convert(GeneratorSettingDTO.class, byTableId);
    }
}
