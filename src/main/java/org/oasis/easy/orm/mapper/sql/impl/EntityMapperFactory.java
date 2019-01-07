package org.oasis.easy.orm.mapper.sql.impl;

import org.oasis.easy.orm.mapper.sql.IEntityMapper;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class EntityMapperFactory {

    public IEntityMapper create(Class<?> clazz) {
        IEntityMapper entityMapper = new EntityMapper(clazz);
        return entityMapper;
    }
}
