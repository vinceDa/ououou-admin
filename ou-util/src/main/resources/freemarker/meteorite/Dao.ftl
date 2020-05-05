package ${package}.dao;

import ${package}.entity.${entityName};
import ${package}.entity.query.${entityName}QueryCriteria;

import java.util.List;

public interface ${entityName}Dao {

    int deleteByPrimaryKey(String id);

    int insert(${entityName} record);

    int insertSelective(${entityName} record);

    ${entityName} selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(${entityName} record);

    int updateByPrimaryKey(${entityName} record);

    List<${entityName}> listForPage(${entityName}QueryCriteria record);

}
