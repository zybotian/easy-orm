package org.oasis.easy.orm.statement;

import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class StatementRuntime {
    private String sql;
    private Object[] args;
    private StatementMetadata metadata;
    private Map<String, Object> parameters;

    public StatementRuntime(StatementMetadata metadata, Map<String, Object> parameters) {
        this.metadata = metadata;
        this.parameters = parameters;
        this.sql = metadata.getSql();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public StatementMetadata getMetadata() {
        return metadata;
    }
}
