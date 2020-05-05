package ${package}.mapper;

import ${package}.entity.${entityName};

public interface ${entityName}Mapper {

    int deleteByPrimaryKey(String id);
    
    int insert(${entityName} record);
    
    int insertSelective(${entityName} record);
    
    ${entityName} selectByPrimaryKey(String id);
    
    int updateByPrimaryKeySelective(${entityName} record);
    
    int updateByPrimaryKey(${entityName} record);

}