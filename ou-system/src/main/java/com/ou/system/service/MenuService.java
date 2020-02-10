package com.ou.system.service;

import com.ou.system.domain.dto.MenuDTO;
import com.ou.system.domain.query.MenuQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author vince
 */
public interface MenuService {

    /**
     *  返回所有的菜单信息
     * @return  返回所有的菜单信息
     */
    List<MenuDTO> listAll();

    /**
     *  分页查询菜单信息
     * @param pageable 分页参数实体类
     * @return 符合条件的菜单信息集合
     */
    List<MenuDTO> listAll(Pageable pageable);

    /**
     *  查询菜单信息
     * @param specification 查询条件
     * @return 符合条件的菜单信息集合
     */
    List<MenuDTO> listAll(Specification<MenuQueryCriteria> specification);

    /**
     *  分页查询菜单信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的菜单信息集合
     */
    List<MenuDTO> listAll(Specification<MenuQueryCriteria> specification, Pageable pageable);

    /**
     *  根据菜单id获取菜单信息
     * @param id  菜单id
     * @return 传入id对应的菜单信息
     */
    MenuDTO getById(Long id);

    /**
     *  根据菜单名称获取对应的菜单信息
     * @param name  菜单名
     * @return 传入的菜单名所对应的菜单信息
     */
    MenuDTO getByName(String name);

    /**
     *  新增菜单
     * @param menuDTO 菜单信息实体
     * @return 新增成功实体类
     */
    MenuDTO insert(MenuDTO menuDTO);

    /**
     *  删除菜单
     * @param id 菜单id
     */
    void delete(Long id);

    /**
     *  更新菜单
     * @param menuDTO 需要更新的菜单信息
     * @return 更新成功的实体类
     */
    MenuDTO update(MenuDTO menuDTO);

    /**
     *  检测菜单id是否存在
     * @param id 菜单id
     * @return 检测结果
     */
    Boolean existsById(Long id);

    /**
     *  根据源数据构建菜单树
     * @param originMenuDTOS 源数据
     * @return 带层级的菜单集合
     */
    List<MenuDTO> buildMenuTree(List<MenuDTO> originMenuDTOS);
}
