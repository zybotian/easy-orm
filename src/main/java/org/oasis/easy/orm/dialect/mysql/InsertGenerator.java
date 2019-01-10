package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-08
 */
public class InsertGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        generatedSql.append(operationMapper.getOperationName());
        generatedSql.append(INTO);
        generatedSql.append(operationMapper.getEntityMapper().getTableName());
        generatedSql.append(BRACKETS_LEFT);
        List<IColumnMapper> columnMappers = operationMapper.getEntityMapper().getColumnMappers();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (IColumnMapper columnMapper : columnMappers) {
            columns.append(columnMapper.getName());
            columns.append(COMMA);
            values.append(":1.");
            values.append(columnMapper.getFieldName());
            values.append(COMMA);
        }
        /**
         * @see org.oasis.easy.orm.mapper.sql.impl.EntityMapper#generateColumnMappers(Class)
         * 这里不需要检查数组越界问题
         */
        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        generatedSql.append(columns);
        generatedSql.append(BRACKETS_RIGHT);
        generatedSql.append(VALUES);
        generatedSql.append(BRACKETS_LEFT);
        generatedSql.append(values);
        generatedSql.append(BRACKETS_RIGHT);
    }

    @Override
    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        // nothing
    }

    @Override
    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        // nothing
    }
}
