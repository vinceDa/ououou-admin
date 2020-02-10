package com.ou.common.constant.sql;

/**
 *  查询的连接条件
 * @author vince
 * @date 2019/10/28 22:27
 */
public enum ConnectConditionEnum {
    /**
     *  =
     */
    EQUAL,
    // 下面四个用于Number类型的比较
    /**
     *  >
     */
    GT,
    /**
     *  >=
     */
    GE,
    /**
     *  <
     */
    LT,
    /**
     *  <=
     */
    LE,
    /**
     *  !=
     */
    NOT_EQUAL,
    /**
     *  like
     */
    LIKE,
    /**
     *  not like
     */
    NOT_LIKE,
    // 下面四个用于可比较类型(Comparable)的比较
    /**
     *  >
     */
    GREATER_THAN,
    /**
     *  >=
     */
    GREATER_THAN_OR_EQUAL_TO,
    /**
     *  <
     */
    LESS_THAN,
    /**
     *  <=
     */
    LESS_THAN_OR_EQUAL_TO
}
