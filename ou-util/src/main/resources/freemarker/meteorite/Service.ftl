package ${package}.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import ${package}.domain.${entityName};
import ${package}.domain.dto.${entityName}DTO;
import ${package}.domain.query.${entityName}QueryCriteria;

/**
 * @author ${author}
 * @date ${date}
 */
public interface ${entityName}Service {

    /**
     *  返回所有的${entityChineseName}信息
     * @return  返回所有的${entityChineseName}信息
     */
    List<${entityName}DTO> listAll();

    /**
     *  根据条件查询${entityChineseName}信息
     * @param specification 查询条件实体类
     * @return 符合条件的${entityChineseName}信息集合
     */
    List<${entityName}DTO> listAll(Specification<${entityName}QueryCriteria> specification);

    /**
     *  分页查询${entityChineseName}信息
     * @param pageable 分页参数实体类
     * @return 符合条件的${entityChineseName}信息集合
     */
    Page<${entityName}> listPageable(Pageable pageable);

    /**
     *  分页查询${entityChineseName}信息
     * @param specification 查询条件
     * @param pageable 分页参数实体类
     * @return 符合条件的${entityChineseName}信息集合
     */
    Page<${entityName}> listPageable(Specification<${entityName}QueryCriteria> specification, Pageable pageable);

    /**
     *  根据${entityChineseName}id获取${entityChineseName}信息
     * @param id  ${entityChineseName}id
     * @return 传入id对应的${entityChineseName}信息
     */
    ${entityName}DTO getById(Long id);

    /**
     *  新增${entityChineseName}
     * @param ${entityName?uncap_first}DTO ${entityChineseName}信息实体
     * @return 新增成功实体类
     */
    ${entityName}DTO insert(${entityName}DTO ${entityName?uncap_first}DTO);

    /**
     *  删除${entityChineseName}
     * @param id ${entityChineseName}id
     */
    void delete(Long id);

    /**
     *  更新${entityChineseName}
     * @param ${entityName?uncap_first}DTO 需要更新的${entityChineseName}信息
     * @return 更新成功的实体类
     */
    ${entityName}DTO update(${entityName}DTO ${entityName?uncap_first}DTO);
}