package ${package}.common.constant;

/**
 *  操作类型
 * @author ${author}
 * @date ${date}
 */
public enum OperationTypeEnum {
    /**
     *  新增
     */
    CREATE("create"),
    /**
     *  删除
     */
    DELETE("delete"),
    /**
     *  修改
     */
    UPDATE("update"),
    /**
     *  查询
     */
    READ("read");

    private String value;

    OperationTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
