package com.ou.generator.domain;

import lombok.Data;

@Data
public class GeneratorTableInfo {

    /**
     *  列名
     */
    private String columnName;

    /**
     *  是否可以为空
     */
    private String isNullable;


    /**
     *  java中的数据类型
     */
    private String javaDataType;

    /**
     *  列描述
     */
    private String columnComment;

    /**
     *  列名(生成模板用), 根据columnName转化成小驼峰命名
     */
    private String columnHumpName;

    /**
     *  数据库中的数据类型
     */
    private String dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getJavaDataType() {
        return javaDataType;
    }

    public void setJavaDataType(String javaDataType) {
        this.javaDataType = javaDataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnHumpName() {
        return columnHumpName;
    }

    public void setColumnHumpName(String columnHumpName) {
        this.columnHumpName = columnHumpName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
