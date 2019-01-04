package org.oasis.easy.orm.mapper.row;

import org.oasis.easy.orm.statement.StatementMetadata;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tianbo
 * @date 2019-01-04
 */
public class ListRowMapper extends AbstractCollectionRowMapper {

    public ListRowMapper(StatementMetadata modifier) {
        super(modifier);
    }

    @Override
    protected Collection createCollection(int columnSize) {
        return new ArrayList(columnSize);
    }
}
