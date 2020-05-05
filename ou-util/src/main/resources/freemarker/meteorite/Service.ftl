package ${package}.service;

import ${package}.entity.${entityName};
import ${package}.entity.query.${entityName}QueryCriteria;

import java.util.List;

/**
 * @author ${author}
 * @date ${date}
 */
public interface ${entityName}Service {

    int deleteByPrimaryKey(String id);

    int insertSelective(${entityName} record);

    ${entityName} selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(${entityName} record);

    List<${entityName}> listForPage(${entityName}QueryCriteria record);

}