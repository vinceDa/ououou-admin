package com.ou.generator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ou.generator.domain.GeneratorSetting;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:55
 */
public interface GeneratorSettingRepository extends JpaRepository<GeneratorSetting, Long>, JpaSpecificationExecutor {

    /**
     *  查询某个表的属性配置
     * @param tableId 表的主键id
     * @return 属性配置实体
     */
    GeneratorSetting getByTableId(Long tableId);

    /**
     *  根据表id删除配置
     * @param tableIds 表id的集合
     */
    void deleteAllByTableIdIn(List<Long> tableIds);

}
