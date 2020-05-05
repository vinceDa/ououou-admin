package ${package}.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author ${author}
 * @date ${date}
 */
@Data
public class ${entityName}DTO {

    <#list fields as field>
    /**
     *  ${field['comment']}
     */
    <#if field['name'] = 'id'>
    @NotNull(groups = Update.class, message = "id不能为空")
    </#if>
    private ${field['packagingType']} ${field['humpName']};
    </#list>

    public interface Insert{};

    public interface Update{};
}
