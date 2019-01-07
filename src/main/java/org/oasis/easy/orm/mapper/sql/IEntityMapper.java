package org.oasis.easy.orm.mapper.sql;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public interface IEntityMapper {
    /**
     * 列的mapper
     */
    List<IColumnMapper> getColumnMappers();

    /**
     * 表的名字
     */
    String getTableName();

    /**
     * 主键对应的列
     */
    List<IColumnMapper> getPrimaryKeyColumnMappers();
}
