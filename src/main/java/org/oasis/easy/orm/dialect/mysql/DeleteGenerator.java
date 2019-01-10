package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2019-01-08
 */
public class DeleteGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
        generatedSql.append(operationMapper.getOperationName());
        generatedSql.append(FROM);
        generatedSql.append(operationMapper.getEntityMapper().getTableName());
    }

    @Override
    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.applyCondition(operationMapper, statementRuntime, generatedSql);
    }

    @Override
    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.afterApplyCondition(operationMapper, statementRuntime, generatedSql);
    }
}
