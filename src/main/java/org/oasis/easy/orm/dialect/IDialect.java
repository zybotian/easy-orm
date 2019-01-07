package org.oasis.easy.orm.dialect;

import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementRuntime;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public interface IDialect {
    <T extends IOperationMapper> String translate(T operationMapper, StatementRuntime statementRuntime);
}
