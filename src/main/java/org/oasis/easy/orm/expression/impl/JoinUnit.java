package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.*;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class JoinUnit implements IExecUnit {
    private final String expr;

    public JoinUnit(String expr) {
        this.expr = expr;
    }

    @Override
    public void fill(IExecContext context, IExprResolver exprResolver) {
        Object result = exprResolver.evaluate(expr);
        context.fillText(String.valueOf(result));
    }

    @Override
    public String toString() {
        return expr;
    }

    public String getExpr() {
        return expr;
    }
}
