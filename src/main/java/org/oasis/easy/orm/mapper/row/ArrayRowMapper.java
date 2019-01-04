package org.oasis.easy.orm.mapper.row;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tianbo
 * @date 2019-01-04
 */
public class ArrayRowMapper implements RowMapper {

    private final Class<?> componentType;

    public ArrayRowMapper(Class<?> returnType) {
        this.componentType = returnType.getComponentType();
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        int columnSize = rs.getMetaData().getColumnCount();
        Object array = Array.newInstance(componentType, columnSize);
        for (int i = 0; i < columnSize; i++) {
            Object value = JdbcUtils.getResultSetValue(rs, (i + 1), componentType);
            Array.set(array, i, value);
        }
        return array;
    }
}
