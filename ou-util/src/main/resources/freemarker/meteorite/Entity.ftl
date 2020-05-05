package ${package}.entity;

import javax.validation.constraints.NotNull;

/**
* @author ${author}
* @date ${date}
*/
public class ${entityName} {

<#list tableInfos as tableInfo>
    /**
     *  ${tableInfo['columnComment']}
     */
    <#if tableInfo['isNullable'] == 'NO'>
        <#if tableInfo['columnName'] == 'id'>
            @NotNull(groups = Update.class, message = "${tableInfo['columnComment']}不能为空")
            <#else>
                @.NotNull(groups = Insert.class, message = "${tableInfo['columnComment']}不能为空")
        </#if>

    </#if>
    private ${tableInfo['javaDataType']} ${tableInfo['columnHumpName']};
</#list>

    public interface Insert{};

    public interface Update{};

<#list tableInfos as tableInfo>

    public String get${tableInfo['columnHumpName']?cap_first}() {
        return ${tableInfo['columnHumpName']};
    }

    public void set${tableInfo['columnHumpName']?cap_first}(String ${tableInfo['columnHumpName']}) {
        this.${tableInfo['columnHumpName']} = ${tableInfo['columnHumpName']} == null ? null : ${tableInfo['columnHumpName']}.trim();
    }
</#list>
}
