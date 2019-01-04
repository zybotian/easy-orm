package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.*;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class EmptyUnit implements IExecUnit {

    @Override
    public void fill(IExecContext context, IExprResolver exprResolver) {
        // do nothing
    }
}
