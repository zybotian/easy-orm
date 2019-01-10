package org.oasis.easy.orm.mapper.sql.impl;

import org.oasis.easy.orm.annotations.SqlParam;

import java.lang.annotation.Annotation;

/**
 * @author tianbo
 * @date 2019-01-09
 */
public class MethodParameter {

    private final SqlParam sqlParam;

    private final Class<?> type;

    private final Annotation[] annotations;

    public MethodParameter(SqlParam sqlParam, Class<?> type, Annotation[] annotations) {
        this.sqlParam = sqlParam;
        this.type = type;
        this.annotations = annotations;
    }

    public SqlParam getSqlParam() {
        return sqlParam;
    }

    public Class<?> getType() {
        return type;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public String getParameterName() {
        return sqlParam != null ? sqlParam.value() : type.getSimpleName();
    }
}
