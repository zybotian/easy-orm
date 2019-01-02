package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.IExecUnit;

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

    public List<IExecUnit> getUnits() {
        return units;
    }
}
