package org.oasis.easy.orm.interpreter;

import org.oasis.easy.orm.expression.IExecContext;
import org.oasis.easy.orm.expression.IExecPattern;
import org.oasis.easy.orm.expression.impl.ExecContext;
import org.oasis.easy.orm.expression.impl.ExecPattern;
import org.oasis.easy.orm.statement.StatementRuntime;
import org.springframework.core.annotation.Order;

/**
 * @author tianbo
 * @date 2019-01-03
 */
@Order(1)
public class SystemInterpreter implements Interpreter {
    private static final int[] EXTEND = {16, 32, 64, 128, 256};

    @Override
    public void interpret(StatementRuntime runtime) {
        IExecPattern pattern = ExecPattern.compile(runtime.getSql());
        IExecContext context = new ExecContext(calculateCapacity(runtime.getSql()));

        pattern.execute(context, runtime.getParameters());
        runtime.setArgs(context.getParams());
        runtime.setSql(context.getContent());
    }

    private int calculateCapacity(String sql) {
        int originSize = sql.length();
        int half = originSize >> 1;
        if (half <= EXTEND[0]) {
            return originSize + EXTEND[0];
        }
        if (half >= EXTEND[EXTEND.length - 1]) {
            return originSize + EXTEND[EXTEND.length - 1];
        }
        for (int i = 0; i < EXTEND.length - 1; i++) {
            if (half >= EXTEND[i] && half <= EXTEND[i + 1]) {
                return originSize + EXTEND[i];
            }
        }
        return originSize + EXTEND[EXTEND.length - 1];
    }
}