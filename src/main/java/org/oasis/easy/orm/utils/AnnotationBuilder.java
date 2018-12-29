package org.oasis.easy.orm.utils;

import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.constant.SqlType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class AnnotationBuilder {

    public static Sql buildSqlAnnotation(final Method method) {
        return new Sql() {
            @Override
            public String value() {
                String toString = method.toString();
                int paramStart = toString.indexOf("(");
                int methodNameStart = toString.lastIndexOf('.', paramStart) + 1;
                return toString.substring(methodNameStart) + "@" + method.getDeclaringClass().getName();
            }

            @Override
            public SqlType type() {
                return SqlType.AUTO_DETECT;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Sql.class;
            }
        };
    }
}
