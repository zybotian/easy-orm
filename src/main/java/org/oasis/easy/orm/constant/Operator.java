package org.oasis.easy.orm.constant;

/**
 * @author tianbo
 * @date 2019-01-09
 */
public enum Operator {
    /**
     * 等于
     */
    EQ,

    /**
     * 不等于
     */
    NE,

    /**
     * 大于
     */
    GE,

    /**
     * 小于
     */
    LE,

    /**
     * 大于等于
     */
    GT,

    /**
     * 小于等于
     */
    LT,

    /**
     * like
     */
    LIKE,

    /**
     * in
     */
    IN,

    /**
     * 查询区间的起始
     */
    OFFSET,

    /**
     * 限制查询条目数
     */
    LIMIT,;
}
