package org.oasis.easy.orm.mapper.row;

import org.oasis.easy.orm.statement.StatementMetadata;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface RowMapperFactory {
    RowMapper<?> getRowMapper(StatementMetadata metadata);
}
