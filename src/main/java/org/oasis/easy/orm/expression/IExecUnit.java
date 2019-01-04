package org.oasis.easy.orm.expression;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public interface IExecUnit {
    void fill(IExecContext context, IExprResolver exprResolver);
}
