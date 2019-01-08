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

    // sql关键词,前后都带上空格
    protected static final String WHERE = " WHERE ";
    protected static final String AND = " AND ";
    protected static final String FROM = " FROM ";

    // sql中的各种符号,前后都不带空格
    protected static final String COMMA = ",";
    protected static final String EQUALS = "=";
    protected static final String COLON = ":";

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
    }

    protected void afterApplyCondition(ConditionOperationMapper operationMapper, StatementRuntime statementRuntime, StringBuilder generatedSql) {
    }
}
