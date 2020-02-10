package com.ou.system.service;

import com.ou.system.domain.Department;
import com.ou.system.domain.dto.DepartmentDTO;
import com.ou.system.domain.query.DepartmentQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author vince
 */
public interface DepartmentService {

    /**
     *  返回所有的部门信息
     * @return  返回所有的部门信息
     */
    List<DepartmentDTO> listAll();

    /**
     *  根据条件查询部门信息
     * @param specification 查询条件实体类
     * @return 符合条件的部门信息集合
     */
    List<DepartmentDTO> listAll(Specification<DepartmentQueryCriteria> specification);

    /**
     *  分页查询部门信息
     * @param pageable 分页参数实体类
     * @return 符合条件的部门信息集合
     */
    Page<Department> listPageable(Pageable pageable);

    /**
     *  分页查询部门信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的部门信息集合
     */
    Page<Department> listPageable(Specification<DepartmentQueryCriteria> specification, Pageable pageable);

    /**
     *  根据部门id获取部门信息
     * @param id  部门id
     * @return 传入id对应的部门信息
     */
    DepartmentDTO getById(Long id);

    /**
     *  根据部门名称获取对应的部门信息
     * @param name  部门名
     * @return 传入的部门名所对应的部门信息
     */
    DepartmentDTO getByName(String name);

    /**
     *  新增部门
     * @param departmentDTO 部门信息实体
     * @return 新增成功实体类
     */
    DepartmentDTO insert(DepartmentDTO departmentDTO);

    /**
     *  删除部门
     * @param id 部门id
     */
    void delete(Long id);

    /**
     *  更新部门
     * @param departmentDTO 需要更新的部门信息
     * @return 更新成功的实体类
     */
    DepartmentDTO update(DepartmentDTO departmentDTO);

    /**
     *  检测部门id是否有误
     * @param id 部门id
     * @return 检测结果
     */
    Boolean existsById(Long id);

    /**
     *  根据源数据构建部门树
     * @param departmentDTOS 源数据
     * @return 带层级的部门集合
     */
    List<DepartmentDTO> buildMenuTree(List<DepartmentDTO> departmentDTOS);

}
