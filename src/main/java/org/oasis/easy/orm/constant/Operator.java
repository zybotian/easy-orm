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
     * 大于等于
     */
    GE,

    /**
     * 小于等于
     */
    LE,

    /**
     * 大于
     */
    GT,

    /**
     * 小于
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
     * 范围查询,查询区间的起始
     */
    OFFSET,

    /**
     * 范围查询,限制查询条目数
     */
    LIMIT,;
}
