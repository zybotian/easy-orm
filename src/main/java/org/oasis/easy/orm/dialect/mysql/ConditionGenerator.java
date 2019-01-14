package org.oasis.easy.orm.dialect.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.constant.Operator;
import org.oasis.easy.orm.dialect.ISqlGenerator;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IParameterMapper;
import org.oasis.easy.orm.mapper.sql.impl.ConditionOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;
import org.oasis.easy.orm.utils.OperatorUtils;

import java.util.List;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public abstract class ConditionGenerator implements ISqlGenerator<ConditionOperationMapper> {

    // sql关键词,前后都带上空格
    protected static final String WHERE = " WHERE ";
    protected static final String AND = " AND ";
    protected static final String FROM = " FROM ";
    protected static final String INTO = " INTO ";
    protected static final String VALUES = " VALUES ";
    protected static final String SET = " SET ";
    protected static final String LIMIT = " LIMIT ";

    // sql中的各种符号,前后都不带空格
    protected static final String COMMA = ",";
    protected static final String EQUALS = "=";
    protected static final String COLON = ":";
    protected static final String BRACKETS_LEFT = "(";
    protected static final String BRACKETS_RIGHT = ")";
    protected static final String IS_NULL = " IS NULL ";
    protected static final String IS_NOT_NULL = " IS NOT NULL ";

    protected static final Map<Operator, String> OPERATORS = OperatorUtils.getOperatorValueMap();
    protected static final String FOR_UPDATE = " FOR UPDATE";

    @Override
    public String generate(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime) {
        StringBuilder generatedSql = new StringBuilder();
        beforeApplyCondition(operationMapper, statementRuntime, generatedSql);
        applyCondition(operationMapper, statementRuntime, generatedSql);
        afterApplyCondition(operationMapper, statementRuntime, generatedSql);
//        System.out.println("generated sql[" + generatedSql + "]");
        return generatedSql.toString();
    }

    protected void beforeApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }

    protected void applyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        if (operationMapper.isPrimaryKeyMode()) {
            applyConditionByPrimaryKeyMode(operationMapper, statementRuntime, generatedSql);
        } else if (operationMapper.isComplexMode()) {
            applyConditionByPrimaryKeyOrComplexMode(operationMapper, statementRuntime, generatedSql);
        } else {
            throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unsupported operation mapper mode");
        }
    }

    private void applyConditionByPrimaryKeyMode(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        generatedSql.append(WHERE);
        List<IColumnMapper> primaryKey = operationMapper.getEntityMapper().getPrimaryKeyColumnMappers();

        for (int i = 0; i < primaryKey.size(); i++) {
            IColumnMapper columnMapper = primaryKey.get(i);
            if (i > 0) {
                generatedSql.append(AND);
            }
            generatedSql.append(columnMapper.getName());
            generatedSql.append(EQUALS);
            generatedSql.append(COLON);
            generatedSql.append(i + 1);
        }
        if (operationMapper.isLockMode()) {
            generatedSql.append(FOR_UPDATE);
        }
    }

    private void applyConditionByPrimaryKeyOrComplexMode(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
        List<IParameterMapper> parameterMappers = operationMapper.getParameterMappers();
        if (CollectionUtils.isEmpty(parameterMappers)) {
            return;
        }

        int index = operationMapper.getWhereAt();
        if (index < 0) {
            // 如果没有标记@Where注解,则默认所有的参数都属于where条件
            index = 0;
        }

        boolean appendedWhere = false;
        String and = "";
        int parameterSize = parameterMappers.size();
        for (; index < parameterSize; index++) {
            String condition = generateCondition(operationMapper, statementRuntime, parameterMappers.get(index), index);
            if (StringUtils.isNotEmpty(condition)) {
                if (!appendedWhere) {
                    generatedSql.append(WHERE);
                    appendedWhere = true;
                }

                generatedSql.append(and);
                generatedSql.append(condition);
                and = AND;
            }
        }
    }

    private String generateCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime,
                                     IParameterMapper parameterMapper, int index) {
        Operator operator = parameterMapper.getOperator();
        if (!OPERATORS.containsKey(operator)) {
            return null;
        }

        Object value = statementRuntime.getParameters().get(":" + (index + 1));
        if (value == null) {
            // 忽略IN NULL
            return null;
        }

        StringBuilder sql = new StringBuilder();
        String fieldName = parameterMapper.getColumnMapper().getFieldName();
        IColumnMapper columnMapper = operationMapper.getEntityMapper().getColumnMapperByFieldName(fieldName);
        sql.append(columnMapper.getName());
        sql.append(OPERATORS.get(operator));

        if (operator == Operator.IN) {
            sql.append(BRACKETS_LEFT);
        }
        sql.append(":");
        sql.append(index + 1);
        if (operator == Operator.IN) {
            sql.append(BRACKETS_RIGHT);
        }
        return sql.toString();
    }

    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }
}
