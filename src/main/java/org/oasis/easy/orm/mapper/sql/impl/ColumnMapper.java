package org.oasis.easy.orm.mapper.sql.impl;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.annotations.Column;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.utils.FormatUtils;

import java.lang.reflect.Field;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class ColumnMapper implements IColumnMapper {

    private final Field field;

    private final Column column;

    public ColumnMapper(Field field) {
        field.setAccessible(true);
        this.field = field;
        this.column = field.getAnnotation(Column.class);
    }

    @Override
    public String getName() {
        String name = column.value();
        // 如果指定了列名字,使用指定的名字,否则认为字段名是小驼峰格式的,转成小写字母下划线分隔的形式即为列名
        return StringUtils.isNotBlank(name) ? name : FormatUtils.lowerCamelToLowerUnderScore(field.getName());
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }
}
