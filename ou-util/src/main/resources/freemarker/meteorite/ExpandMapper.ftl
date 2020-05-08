package freemarker.meteorite;

${package}.mapper;

import ${package}.entity.${entityName};
import ${package}.entity.query.${entityName}QueryCriteria;

import java.util.List;

public interface ${entityName}ExpandMapper {

    List<${entityName}> listForPage(${entityName}QueryCriteria record);

}