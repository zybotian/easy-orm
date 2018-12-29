package org.oasis.easy.orm.statement;

import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface Statement {
    StatementMetadata getMetadata();

    Object execute(Map<String, Object> parameters);
}
