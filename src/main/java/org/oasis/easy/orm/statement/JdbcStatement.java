package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.constant.SqlType;
import org.oasis.easy.orm.interpreter.Interpreter;

import java.util.List;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class JdbcStatement implements Statement {

    private final StatementMetadata metaData;

    private final List<Interpreter> interpreters;

    private final Querier querier;

    private final SqlType sqlType;

    private boolean batchUpdate;

    public JdbcStatement(StatementMetadata metaData,
                         SqlType sqlType,
                         List<Interpreter> interpreters,
                         Querier querier) {
        this.metaData = metaData;
        this.interpreters = interpreters;
        this.sqlType = sqlType;
        this.querier = querier;
        this.batchUpdate = true;
    }

    @Override
    public StatementMetadata getMetadata() {
        return null;
    }

    @Override
    public Object execute(Map<String, Object> parameters) {
        return null;
    }
}
