package com.ou.system.repository;

import com.ou.system.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vince
 */
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor {

    /**
     *  根据岗位名查询岗位信息
     * @param name 岗位名
     * @return 岗位信息
     */
    Job getByName(String name);

    /**
     *  列举部门下所有的岗位
     * @param departmentId 部门id
     * @return 符合条件的岗位集合
     */
    List<Job> findJobByDepartmentIdAndEnableIsTrue(Long departmentId);
}
