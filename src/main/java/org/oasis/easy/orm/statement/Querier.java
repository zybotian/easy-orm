package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.constant.SqlType;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public interface Querier {
    Object execute(SqlType sqlType, StatementRuntime... runtimes);
}
