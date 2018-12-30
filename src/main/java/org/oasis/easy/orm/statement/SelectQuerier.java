package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.constant.SqlType;
import org.oasis.easy.orm.converter.*;
import org.oasis.easy.orm.data.access.DataAccess;
import org.oasis.easy.orm.data.access.DataAccessFactory;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class SelectQuerier implements Querier {

    private final DataAccessFactory dataAccessFactory;

    private final RowMapper rowMapper;

    private final Class<?> returnType;

    private final ResultConverter converter;

    public SelectQuerier(DataAccessFactory dataAccessFactory, StatementMetadata metaData, RowMapper rowMapper) {
        this.dataAccessFactory = dataAccessFactory;
        this.rowMapper = rowMapper;
        this.converter = getResultConverter();
        this.returnType = metaData.getClass();
    }

    @Override
    public Object execute(SqlType sqlType, StatementRuntime... runtimes) {
        return execute(sqlType, runtimes[0]);
    }

    private Object execute(SqlType sqlType, StatementRuntime runtime) {
        DataAccess dataAccess = dataAccessFactory.getDataAccess();
        List<?> listResult = dataAccess.select(runtime.getSql(), runtime.getArgs(), rowMapper);
        return converter.convert(listResult, returnType);
    }

    private ResultConverter getResultConverter() {
        ResultConverter converter = ResultConverterFactory.getInstance(returnType);
        if (converter != null) {
            return converter;
        }
        if (Collection.class.isAssignableFrom(returnType)) {
            converter = ResultConverterFactory.newInstance(CollectionAssignableResultConverter.class);
        } else if (returnType.isArray() && byte[].class != returnType) {
            if (returnType.getComponentType().isPrimitive()) {
                converter = ResultConverterFactory.newInstance(ArrayTypePrimitiveResultConverter.class);
            } else {
                converter = ResultConverterFactory.newInstance(ArrayTypeNonePrimitiveResultConverter.class);
            }
        } else if (Map.class.isAssignableFrom(returnType)) {
            converter = ResultConverterFactory.newInstance(MapAssignableResultConverter.class);
        } else {
            converter = ResultConverterFactory.newInstance(DefaultResultConverter.class);
        }
        return converter;
    }
}

