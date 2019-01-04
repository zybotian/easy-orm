package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.*;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class BunchUnit implements IExecUnit {
    private List<IExecUnit> units;

    public BunchUnit(List<IExecUnit> units) {
        this.units = units;
    }

    @Override
    public void fill(IExecContext context, IExprResolver exprResolver) {
        for (IExecUnit execUnit : units) {
            execUnit.fill(context, exprResolver);
        }
    }

    public List<IExecUnit> getUnits() {
        return units;
    }
}
