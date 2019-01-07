package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.dialect.ISqlGenerator;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.List;

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
        if (operationMapper.isPrimaryKeyMode()) {
            applyConditionByPrimaryKeyMode(operationMapper, statementRuntime, generatedSql);
        } else {
            throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unsupported operation mapper mode");
        }
    }

    private void applyConditionByPrimaryKeyMode(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        generatedSql.append(" where ");
        List<IColumnMapper> primaryKey = operationMapper.getEntityMapper().getPrimaryKeyColumnMappers();

        for (int i = 0; i < primaryKey.size(); i++) {
            IColumnMapper columnMapper = primaryKey.get(i);
            if (i > 0) {
                generatedSql.append(" AND ");
            }
            generatedSql.append(columnMapper.getName());
            generatedSql.append(" = ");
            generatedSql.append(":");
            generatedSql.append(i + 1);
        }
    }

    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }
}
