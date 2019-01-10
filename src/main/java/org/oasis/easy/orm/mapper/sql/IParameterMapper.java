package org.oasis.easy.orm.mapper.sql;

import org.oasis.easy.orm.constant.Operator;

/**
 * @author tianbo
 * @date 2019-01-09
 */
public interface IParameterMapper {
    String getParameterName();

    IColumnMapper getColumnMapper();

    Class<?> getType();

    Operator getOperator();
}
