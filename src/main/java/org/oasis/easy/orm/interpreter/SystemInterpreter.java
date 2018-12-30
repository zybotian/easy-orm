package org.oasis.easy.orm.interpreter;

import org.oasis.easy.orm.statement.StatementRuntime;
import org.springframework.core.annotation.Order;

@Order(1)
public class SystemInterpreter implements Interpreter {

    @Override
    public void interpret(StatementRuntime runtime) {

    }
}