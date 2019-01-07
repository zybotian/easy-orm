package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.mapper.sql.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class SelectGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
    }

    @Override
    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.applyCondition(operationMapper, statementRuntime, generatedSql);
        generatedSql.append("select * from user where id=1");
    }

    @Override
    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.afterApplyCondition(operationMapper, statementRuntime, generatedSql);
    }
}
