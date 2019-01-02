package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.IExecUnit;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExprUnit implements IExecUnit {
    private final String expr;

    public ExprUnit(String expr) {
        this.expr = expr;
    }

    public String getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return expr;
    }
}
