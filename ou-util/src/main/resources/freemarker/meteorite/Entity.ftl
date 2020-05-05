package ${package}.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import ${package}.common.base.BaseDO;

import lombok.Data;

/**
* @author ${author}
* @date ${date}
*/
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "${tableName}")
public class ${entityName} extends BaseDO {

<#list fields as field>
    <#if field['humpName'] != 'id' && field['humpName'] != 'createUserId' &&  field['humpName'] != 'updateUserId' &&
    field['humpName'] != 'createTime' &&  field['humpName'] != 'updateTime'>
        /**
         *  ${field['comment']}
         */
        @Column(name = "${field['name']}")
        private ${field['packagingType']} ${field['humpName']};
    </#if>
</#list>
}
