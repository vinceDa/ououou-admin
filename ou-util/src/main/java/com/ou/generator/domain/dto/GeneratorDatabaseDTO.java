package com.ou.generator.domain.dto;

import javax.validation.constraints.NotNull;

import com.ou.generator.domain.GeneratorTable;
import lombok.Data;

import java.util.List;

/**
 * @author vince
 * @date 2019/12/6 16:09
 */
@Data
public class GeneratorDatabaseDTO {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class, message = "数据库id不能为空")
    private Long id;

    /**
     *  数据库连接id
     */
    @NotNull(groups = Insert.class, message = "数据库连接id不能为空")
    private Long connectionId;

    /**
     *  数据库名
     */
    @NotNull(groups = Insert.class, message = "数据库名不能为空")
    private String name;

    /**
     *  生成树时需要的uniqueKey
     */
    private String key;

    /**
     *  数据库下的表
     */
    private List<GeneratorTableDTO> children;

    public interface Insert {
    }

    public interface Update {
    }
}
