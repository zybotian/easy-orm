package org.oasis.easy.orm.statement;

import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class JdbcStatement implements Statement {

    @Override
    public StatementMetadata getMetadata() {
        return null;
    }

    @Override
    public Object execute(Map<String, Object> parameters) {
        return null;
    }
}
