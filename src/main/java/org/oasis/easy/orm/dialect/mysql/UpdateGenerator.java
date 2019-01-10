package org.oasis.easy.orm.dialect.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.constant.Operator;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IParameterMapper;
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
        generatedSql.append(operationMapper.getOperationName());
        generatedSql.append(StringUtils.SPACE);
        generatedSql.append(operationMapper.getEntityMapper().getTableName());
    }

    @Override
    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        if (operationMapper.isEntityMode() || operationMapper.isEntityCollectionMode()) {
            applyConditionByEntityOrCollectionMode(operationMapper, generatedSql);
        } else {
            applyConditionByComplexMode(operationMapper, generatedSql);
        }
    }

    private void applyConditionByEntityOrCollectionMode(ConditionOperationMapper operationMapper, StringBuilder generatedSql) {
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

    private void applyConditionByComplexMode(ConditionOperationMapper operationMapper, StringBuilder generatedSql) {
        List<IParameterMapper> parameterMappers = operationMapper.getParameterMappers();
        if (CollectionUtils.isEmpty(parameterMappers)) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "update fields missing");
        }

        int whereAt = operationMapper.getWhereAt();
        if (whereAt < 0) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "update operation missing @Where");
        }

        generatedSql.append(SET);
        for (int index = 0; index < whereAt; index++) {
            generatedSql.append(parameterMappers.get(index).getColumnMapper().getName());
            generatedSql.append(EQUALS);
            generatedSql.append(":" + (index + 1));
            generatedSql.append(COMMA);
        }
        generatedSql.setLength(generatedSql.length() - 1);
    }

    @Override
    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        if (operationMapper.isEntityMode() || operationMapper.isEntityCollectionMode()) {
            afterApplyConditionByEntityOrCollectionMode(operationMapper, generatedSql);
        } else {
            afterApplyConditionByPrimaryKeyOrComplexMode(operationMapper, statementRuntime, generatedSql);
        }
    }

    private void afterApplyConditionByEntityOrCollectionMode(ConditionOperationMapper operationMapper, StringBuilder generatedSql) {
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

    private void afterApplyConditionByPrimaryKeyOrComplexMode(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        int whereAt = operationMapper.getWhereAt();
        if (whereAt < 0) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "update operation missing @Where");
        }

        List<IParameterMapper> parameterMappers = operationMapper.getParameterMappers();
        if (CollectionUtils.isEmpty(parameterMappers)) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "update fields missing");
        }

        if (whereAt > parameterMappers.size() - 1) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "update operation missing @Where");
        }
        generatedSql.append(WHERE);
        String and = "";
        for (int i = whereAt; i < parameterMappers.size(); i++) {
            IParameterMapper parameterMapper = parameterMappers.get(i);
            IColumnMapper columnMapper = parameterMapper.getColumnMapper();
            Operator operator = parameterMapper.getOperator();
            generatedSql.append(and);
            generatedSql.append(columnMapper.getName());
            generatedSql.append(OPERATORS.get(operator));
            if (operator == Operator.IN) {
                generatedSql.append(BRACKETS_LEFT);
            }
            generatedSql.append(":" + (i + 1));
            if (operator == Operator.IN) {
                generatedSql.append(BRACKETS_RIGHT);
            }
            and = AND;
        }
    }
}
