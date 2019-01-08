package org.oasis.easy.orm.dialect.mysql;

import org.oasis.easy.orm.dialect.IDialect;
import org.oasis.easy.orm.dialect.ISqlGenerator;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class MySqlDialect implements IDialect {

    private final Map<String, ISqlGenerator<? extends IOperationMapper>> generators;

    public MySqlDialect() {
        // bean是单例模式,不需要使用并发map
        generators = new HashMap<>();
        generators.put(IOperationMapper.OPERATION_SELECT, new SelectGenerator());
        generators.put(IOperationMapper.OPERATION_DELETE, new DeleteGenerator());
    }

    @Override
    public <T extends IOperationMapper> String translate(T operationMapper, StatementRuntime statementRuntime) {
        String operationName = operationMapper.getOperationName();
        if (!generators.keySet().contains(operationName)) {
            throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, operationName + " is not supported");
        }
        ISqlGenerator<T> iSqlGenerator = (ISqlGenerator<T>) generators.get(operationName);
        return iSqlGenerator.generate(operationMapper, statementRuntime);
    }
}
