package org.oasis.easy.orm.dialect.mysql;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IEntityMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class SelectGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        super.beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
        IEntityMapper entityMapper = operationMapper.getEntityMapper();

        List<IColumnMapper> columnMappers = entityMapper.getColumnMappers();
        generatedSql.append(operationMapper.getOperationName());
        generatedSql.append(StringUtils.SPACE);

        for (IColumnMapper columnMapper : columnMappers) {
            generatedSql.append(columnMapper.getName() + COMMA);
        }

        generatedSql.setLength(generatedSql.length() - 1);
        generatedSql.append(FROM + entityMapper.getTableName());
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
