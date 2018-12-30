package org.oasis.easy.orm.interpreter;

import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface Interpreter {
    void interpret(StatementRuntime runtime);
}
