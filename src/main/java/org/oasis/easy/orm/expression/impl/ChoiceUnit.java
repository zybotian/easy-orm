package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.*;
import org.oasis.easy.orm.utils.ExecUtils;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ChoiceUnit implements IExecUnit {

    private final String expr;

    private final IExecUnit ifUnit, elseUnit;


    public ChoiceUnit(String expr, IExecUnit ifUnit, IExecUnit elseUnit) {
        this.expr = expr;
        this.ifUnit = ifUnit;
        this.elseUnit = elseUnit;
    }

    public ChoiceUnit(String expr, IExecUnit ifUnit) {
        this(expr, ifUnit, null);
    }

    @Override
    public void fill(IExecContext context, IExprResolver exprResolver) {
        Object obj = exprResolver.evaluate(expr);

        if (ExecUtils.isValid(obj)) {
            ifUnit.fill(context, exprResolver);
        } else if (elseUnit != null) {
            elseUnit.fill(context, exprResolver);
        }
    }

    public String getExpr() {
        return expr;
    }

    public IExecUnit getIfUnit() {
        return ifUnit;
    }

    public IExecUnit getElseUnit() {
        return elseUnit;
    }

    @Override
    public String toString() {
        return "ChoiceUnit{" +
                "expr='" + expr + '\'' +
                ", ifUnit=" + ifUnit +
                ", elseUnit=" + elseUnit +
                '}';
    }
}
