package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.dialect.ISqlGenerator;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public abstract class ConditionGenerator implements ISqlGenerator<ConditionOperationMapper> {

    @Override
    public String generate(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime) {
        StringBuilder generatedSql = new StringBuilder();
        beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
        applyCondition(operationMapper, statementRuntime, generatedSql);
        afterApplyCondition(operationMapper, statementRuntime, generatedSql);
        return generatedSql.toString();
    }

    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }

    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }

    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }
}
