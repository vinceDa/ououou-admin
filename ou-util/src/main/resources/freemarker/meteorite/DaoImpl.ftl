package ${package}.dao.impl;

import ${package}.dao.${entityName}Dao;
import ${package}.entity.${entityName};
import ${package}.entity.query.${entityName}QueryCriteria;
import ${package}.mapper.${entityName}ExpandMapper;
import ${package}.mapper.${entityName}Mapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("${entityName?uncap_first}Dao")
public class ${entityName}DaoImpl implements ${entityName}Dao {

    @Resource
    private ${entityName}Mapper ${entityName?uncap_first}Mapper;
    
    @Resource
    private ${entityName}ExpandMapper ${entityName?uncap_first}ExpandMapper;
    
    @Override
    public int deleteByPrimaryKey(String id) {
        return ${entityName?uncap_first}Mapper.deleteByPrimaryKey(id);
    }
    
    @Override
    public int insert(${entityName} record) {
        return ${entityName?uncap_first}Mapper.insert(record);
    }
    
    @Override
    public int insertSelective(${entityName} record) {
        return ${entityName?uncap_first}Mapper.insertSelective(record);
    }
    
    @Override
    public ${entityName} selectByPrimaryKey(String id) {
        return ${entityName?uncap_first}Mapper.selectByPrimaryKey(id);
    }
    
    @Override
    public int updateByPrimaryKeySelective(${entityName} record) {
     return ${entityName?uncap_first}Mapper.updateByPrimaryKeySelective(record);
    }
    
    @Override
    public int updateByPrimaryKey(${entityName} record) {
        return ${entityName?uncap_first}Mapper.updateByPrimaryKey(record);
    }
    
    @Override
    public List<${entityName}> listForPage(${entityName}QueryCriteria record) {
        return ${entityName?uncap_first}ExpandMapper.listForPage(record);
    }
    
}
