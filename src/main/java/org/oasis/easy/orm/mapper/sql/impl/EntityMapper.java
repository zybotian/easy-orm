package org.oasis.easy.orm.mapper.sql.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.annotations.Column;
import org.oasis.easy.orm.annotations.Table;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IEntityMapper;
import org.oasis.easy.orm.utils.FormatUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class EntityMapper implements IEntityMapper {

    private List<IColumnMapper> columnMappers;

    private List<IColumnMapper> primaryKeyColumnMappers;

    private String tableName;

    public EntityMapper(Class<?> clazz) {
        columnMappers = generateColumnMappers(clazz);
        tableName = generateTableName(clazz);
        primaryKeyColumnMappers = extractPrimaryKeyColumnMappers();
    }

    private List<IColumnMapper> extractPrimaryKeyColumnMappers() {
        primaryKeyColumnMappers = new LinkedList<>();
        if (CollectionUtils.isEmpty(columnMappers)) {
            return primaryKeyColumnMappers;
        }

        for (IColumnMapper columnMapper : columnMappers) {
            if (columnMapper.isPrimaryKey()) {
                primaryKeyColumnMappers.add(columnMapper);
            }
        }

        return primaryKeyColumnMappers;
    }

    private List<IColumnMapper> generateColumnMappers(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM, "entity has no declared fields");
        }

        columnMappers = new LinkedList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                /**
                 * @see Column
                 * 只看Column注解标记的字段
                 */
                ColumnMapper columnMapper = new ColumnMapper(field);
                columnMappers.add(columnMapper);
            }
        }
        return columnMappers;
    }

    private String generateTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return StringUtils.isNotEmpty(table.value()) ? table.value() : FormatUtils.upperCamelToLowerUnderScore(clazz.getSimpleName());
    }

    @Override
    public List<IColumnMapper> getColumnMappers() {
        return columnMappers;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public List<IColumnMapper> getPrimaryKeyColumnMappers() {
        return primaryKeyColumnMappers;
    }
}
