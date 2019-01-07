package org.oasis.easy.orm.dialect;

import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public interface ISqlGenerator<T extends IOperationMapper> {
    /**
     * 动态生成sql语句
     */
    String generate(T operationMapper, StatementRuntime statementRuntime);
}
