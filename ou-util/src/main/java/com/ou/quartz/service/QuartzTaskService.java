package com.ou.quartz.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ou.quartz.domain.QuartzTask;
import com.ou.quartz.domain.dto.QuartzTaskDTO;
import com.ou.quartz.domain.query.QuartzTaskQueryCriteria;

/**
 * @author vince
 * @date 2020/02/02 07:24:34
 */
public interface QuartzTaskService {

    /**
     * 返回所有的定时任务信息
     * 
     * @return 返回所有的定时任务信息
     */
    List<QuartzTaskDTO> listAll();

    /**
     * 根据条件查询定时任务信息
     * 
     * @param specification
     *            查询条件实体类
     * @return 符合条件的定时任务信息集合
     */
    List<QuartzTaskDTO> listAll(Specification<QuartzTaskQueryCriteria> specification);

    /**
     * 分页查询定时任务信息
     * 
     * @param pageable
     *            分页参数实体类
     * @return 符合条件的定时任务信息集合
     */
    Page<QuartzTask> listPageable(Pageable pageable);

    /**
     * 分页查询定时任务信息
     * 
     * @param specification
     *            查询条件
     * @param pageable
     *            分页参数实体类
     * @return 符合条件的定时任务信息集合
     */
    Page<QuartzTask> listPageable(Specification<QuartzTaskQueryCriteria> specification, Pageable pageable);

    /**
     * 根据定时任务id获取定时任务信息
     * 
     * @param id
     *            定时任务id
     * @return 传入id对应的定时任务信息
     */
    QuartzTaskDTO getById(Long id);

    /**
     * 新增定时任务
     * 
     * @param quartzTaskDTO
     *            定时任务信息实体
     * @return 新增成功实体类
     */
    QuartzTaskDTO insert(QuartzTaskDTO quartzTaskDTO);

    /**
     * 删除定时任务
     * 
     * @param id
     *            定时任务id
     */
    void delete(Long id);

    /**
     * 更新定时任务
     * 
     * @param quartzTaskDTO
     *            需要更新的定时任务信息
     * @return 更新成功的实体类
     */
    QuartzTaskDTO update(QuartzTaskDTO quartzTaskDTO);

    /**
     *  启动定时任务
     * @param id 定时任务主键id
     * @return 启动结果
     */
    Boolean startQuartzTask(Long id);

    /**
     *  暂停定时任务
     * @param id 定时任务主键id
     * @return 暂停结果
     */
    Boolean pauseQuartzTask(Long id);

    /**
     *  重启定时任务
     * @param id 定时任务主键id
     * @return 重启结果
     */
    Boolean resumeQuartzTask(Long id);
    
}