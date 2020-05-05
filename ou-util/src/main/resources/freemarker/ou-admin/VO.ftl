package ${package}.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author ${author}
 * @date ${date}
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${entityName}VO {

<#list fields as field>
/**
 *  ${field['comment']}
 */
private ${field['packagingType']} ${field['humpName']};
</#list>
}
