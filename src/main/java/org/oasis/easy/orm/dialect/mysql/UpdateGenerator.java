package org.oasis.easy.orm.dialect.mysql;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-08
 */
public class UpdateGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
        generatedSql.append(operationMapper.getOperationName());
        generatedSql.append(StringUtils.SPACE);
        generatedSql.append(operationMapper.getEntityMapper().getTableName());
    }

    @Override
    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        generatedSql.append(SET);
        List<IColumnMapper> columnMappers = operationMapper.getEntityMapper().getColumnMappers();
        int columnSize = columnMappers.size();
        for (int i = 0; i < columnSize; i++) {
            IColumnMapper columnMapper = columnMappers.get(i);
            generatedSql.append(columnMapper.getName());
            generatedSql.append(EQUALS);
            generatedSql.append(":1.");
            generatedSql.append(columnMapper.getFieldName());
            generatedSql.append(COMMA);
        }
        generatedSql.setLength(generatedSql.length() - 1);
    }

    @Override
    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.afterApplyCondition(operationMapper, statementRuntime, generatedSql);
        generatedSql.append(WHERE);
        List<IColumnMapper> primaryKeys = operationMapper.getEntityMapper().getPrimaryKeyColumnMappers();

        int primaryKeySize = primaryKeys.size();
        for (int i = 0; i < primaryKeySize; i++) {
            IColumnMapper columnMapper = primaryKeys.get(i);
            if (i > 0) {
                generatedSql.append(AND);
            }
            generatedSql.append(columnMapper.getName());
            generatedSql.append(EQUALS);
            generatedSql.append(":1.");
            generatedSql.append(columnMapper.getFieldName());
        }
    }
}
