package org.oasis.easy.orm.mapper.sql.impl;

import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementMetadata;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class OperationMapperFactory {

    private EntityMapperFactory entityMapperFactory;

    public OperationMapperFactory() {
        this.entityMapperFactory = new EntityMapperFactory();
    }

    public IOperationMapper create(StatementMetadata statementMetadata) {
        return new ConditionOperationMapper(statementMetadata, this.entityMapperFactory);
    }
}
