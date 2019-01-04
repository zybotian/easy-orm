package org.oasis.easy.orm.expression;

import org.oasis.easy.orm.exception.EasyOrmException;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public interface IExprResolver {
    /**
     * 计算表达式的值
     */
    Object evaluate(String expr) throws EasyOrmException;
}
