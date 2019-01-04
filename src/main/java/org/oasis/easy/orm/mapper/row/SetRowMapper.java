package org.oasis.easy.orm.mapper.row;

import org.oasis.easy.orm.statement.StatementMetadata;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author tianbo
 * @date 2019-01-04
 */
public class SetRowMapper extends AbstractCollectionRowMapper {

    public SetRowMapper(StatementMetadata modifier) {
        super(modifier);
    }

    @Override
    protected Collection createCollection(int columnSize) {
        return new HashSet(columnSize);
    }
}
