package org.oasis.easy.orm.statement;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class StatementRuntime {
    private String sql;
    private Object[] args;

    public String getSql() {
        return sql;
    }

    public Object[] getArgs() {
        return args;
    }
}
