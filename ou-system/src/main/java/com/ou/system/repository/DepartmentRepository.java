package com.ou.system.repository;

import com.ou.system.domain.Department;
import com.ou.system.domain.Job;
import com.ou.system.domain.dto.JobDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author vince
 */
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor {

    /**
     *  根据部门名查询部门信息
     * @param name 部门名
     * @return 部门信息
     */
    Department getByName(String name);

}
