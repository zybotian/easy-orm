package org.oasis.easy.orm.dialect.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IEntityMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.List;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class SelectGenerator extends ConditionGenerator {

    @Override
    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
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
        applyPagination(operationMapper, statementRuntime, generatedSql);
    }

    private void applyPagination(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        Map<String, Object> parameters = statementRuntime.getParameters();

        Number offset = null;
        Number limit = null;

        if (operationMapper.getOffsetAt() >= 0) {
            offset = (Number) parameters.get(":" + (operationMapper.getOffsetAt() + 1));
        }

        if (operationMapper.getLimitAt() >= 0) {
            limit = (Number) parameters.get(":" + (operationMapper.getLimitAt() + 1));
        }

        if (limit == null && offset == null) {
            // 不需要分页查询的情况
            return;
        }

        int paramsSize = CollectionUtils.size(parameters);

        /**
         * 这里简单处理,往参数map中append两个参数:
         * :(paramSize+1)作为offset的取值, :(paramSize+2)作为pageSize的取值
         */
        generatedSql.append(LIMIT);
        generatedSql.append(COLON);
        generatedSql.append(paramsSize + 1);
        generatedSql.append(COMMA);
        generatedSql.append(COLON);
        generatedSql.append(paramsSize + 2);

        if (limit != null && offset != null) {
            parameters.put(COLON + (paramsSize + 1), offset);
            parameters.put(COLON + (paramsSize + 2), limit);
        } else if (offset != null) {
            parameters.put(COLON + (paramsSize + 1), offset);
            // 不传pageSize时,pageSize设置为-1,表示所有
            parameters.put(COLON + (paramsSize + 2), -1);
        } else if (limit != null && limit.longValue() >= 0) {
            // 不传offset时,offset设置为0,表示从0行开始取
            parameters.put(COLON + (paramsSize + 1), 0);
            parameters.put(COLON + (paramsSize + 2), limit);
        }
    }
}
