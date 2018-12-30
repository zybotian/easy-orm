package org.oasis.easy.orm.interpreter;

import org.oasis.easy.orm.statement.StatementRuntime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;

@Order(-1)
public class SqlManagerInterpreter implements Interpreter, InitializingBean {

    /**
     * 透传SQL
     */
    private static final Interpreter PassThroughInterpreter = new Interpreter() {
        @Override
        public void interpret(StatementRuntime runtime) {
        }
    };

    @Override
    public void interpret(StatementRuntime runtime) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * SQL构造
     */
    private class SqlGeneratorInterpreter implements Interpreter {
        @Override
        public void interpret(StatementRuntime runtime) {
        }
    }
}
