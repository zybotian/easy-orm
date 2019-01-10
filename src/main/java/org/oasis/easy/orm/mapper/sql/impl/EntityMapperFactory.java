package org.oasis.easy.orm.mapper.sql.impl;

import org.oasis.easy.orm.mapper.sql.AbstractCacheableMap;
import org.oasis.easy.orm.mapper.sql.IEntityMapper;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class EntityMapperFactory extends AbstractCacheableMap<Class<?>, EntityMapper> {

    @Override
    protected EntityMapper newValueObject(Class<?> clazz) {
        return new EntityMapper(clazz);
    }

    public IEntityMapper create(Class<?> clazz) {
        return getOrCreate(clazz);
    }
}
