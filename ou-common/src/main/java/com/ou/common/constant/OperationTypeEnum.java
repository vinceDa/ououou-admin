package com.ou.common.constant;

/**
 *  操作类型
 * @author vince
 * @date 2019/10/15 22:17
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
