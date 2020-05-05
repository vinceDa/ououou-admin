package ${package}.service.impl;

import ${package}.dao.${entityName}Dao;
import ${package}.entity.${entityName};
import ${package}.entity.query.${entityName}QueryCriteria;
import ${package}.service.${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ${author}
 * @date ${date}
 */
@Service("${entityName?uncap_first}ServiceImpl")
public class ${entityName}ServiceImpl implements ${entityName}Service {

    @Autowired
    private ${entityName}Dao ${entityName?uncap_first}Dao;

    @Override
    public int deleteByPrimaryKey(String id) {
        return ${entityName?uncap_first}Dao.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(${entityName} record) {
        return ${entityName?uncap_first}Dao.insertSelective(record);
    }

    @Override
    public ${entityName} selectByPrimaryKey(String id) {
        return ${entityName?uncap_first}Dao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(${entityName} record) {
        return ${entityName?uncap_first}Dao.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<${entityName}> listForPage(${entityName}QueryCriteria record) {
        return ${entityName?uncap_first}Dao.listForPage(record);
    }

}