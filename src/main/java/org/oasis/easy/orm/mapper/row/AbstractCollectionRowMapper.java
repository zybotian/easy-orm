package org.oasis.easy.orm.mapper.row;

import org.apache.commons.lang3.ArrayUtils;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.statement.StatementMetadata;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author tianbo
 * @date 2019-01-04
 */
public abstract class AbstractCollectionRowMapper implements RowMapper {

    private Class<?> elementType;

    public AbstractCollectionRowMapper(StatementMetadata modifier) {
        Class<?>[] genericTypes = modifier.getGenericReturnTypes();
        if (ArrayUtils.isEmpty(genericTypes)) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "generic return type is missing");
        }
        elementType = genericTypes[0];
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        int columnSize = rs.getMetaData().getColumnCount();
        Collection collection = createCollection(columnSize);
        // columnIndex从1开始
        for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {
            collection.add(JdbcUtils.getResultSetValue(rs, columnIndex, elementType));
        }
        return collection;
    }

    /**
     * 由子类覆盖此方法，提供一个空的具体集合实现类
     */
    protected abstract Collection createCollection(int columnSize);

}
