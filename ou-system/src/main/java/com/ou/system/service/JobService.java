package com.ou.system.service;

import com.ou.system.domain.Job;
import com.ou.system.domain.dto.JobDTO;
import com.ou.system.domain.query.JobQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author vince
 */
public interface JobService {

    /**
     *  返回所有的岗位信息
     * @return  返回所有的岗位信息
     */
    List<JobDTO> listAll();

    /**
     *  根据条件查询岗位信息
     * @param specification 查询条件实体类
     * @return 符合条件的岗位信息集合
     */
    List<JobDTO> listAll(Specification<JobQueryCriteria> specification);

    /**
     *  分页查询岗位信息
     * @param pageable 分页参数实体类
     * @return 符合条件的岗位信息集合
     */
    Page<Job> listPageable(Pageable pageable);

    /**
     *  分页查询岗位信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的岗位信息集合
     */
    Page<Job> listPageable(Specification<JobQueryCriteria> specification, Pageable pageable);

    /**
     *  根据岗位id获取岗位信息
     * @param id  岗位id
     * @return 传入id对应的岗位信息
     */
    JobDTO getById(Long id);

    /**
     *  根据岗位名称获取对应的岗位信息
     * @param name  岗位名
     * @return 传入的岗位名所对应的岗位信息
     */
    JobDTO getByName(String name);

    /**
     *  新增岗位
     * @param jobDTO 岗位信息实体
     * @return 新增成功的实体类
     */
    JobDTO insert(JobDTO jobDTO);

    /**
     *  删除岗位
     * @param id 岗位id
     */
    void delete(Long id);

    /**
     *  更新岗位
     * @param jobDTO 需要更新的岗位信息
     * @return 更新成功的实体类
     */
    JobDTO update(JobDTO jobDTO);

    /**
     *  列举部门下所有的岗位
     * @param departmentId 部门id
     * @return 符合条件的岗位集合
     */
    List<JobDTO> listJobs(Long departmentId);
}
