package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.expression.*;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class TextUnit implements IExecUnit {
    private final String text;

    public TextUnit(String text) {
        this.text = text;
    }

    @Override
    public void fill(IExecContext context, IExprResolver exprResolver) {
        context.fillText(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
