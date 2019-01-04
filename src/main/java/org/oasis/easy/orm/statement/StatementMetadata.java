package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.annotations.SqlParam;
import org.oasis.easy.orm.constant.SqlType;
import org.oasis.easy.orm.utils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class StatementMetadata {

    private final DaoMetadata daoMetadata;
    private final Method method;
    private final String sql;
    private final SqlType sqlType;
    /**
     * 方法的返回类型,如果是List<Integer>,这里对应List
     */
    private final Class returnType;
    /**
     * 方法的返回类型,如果是List<Integer>,这里对应Integer
     */
    private final Class[] genericType;
    private final SqlParam[] sqlParams;

    public StatementMetadata(DaoMetadata daoMetaData, Method method) {
        this.daoMetadata = daoMetaData;
        this.method = method;
        // 获取标记在方法上的@Sql注解
        Sql sqlAnnotation = method.getAnnotation(Sql.class);
        if (sqlAnnotation == null) {
            // 如果方法上没有标记@Sql注解,生成一个默认的@Sql注解
            sqlAnnotation = AnnotationBuilder.buildSqlAnnotation(method);
        }
        this.sql = sqlAnnotation.value();
        this.sqlType = resolveSqlType(sqlAnnotation);
        this.returnType = GenericUtils.resolveTypeVariable(daoMetaData.getDaoClass(), method.getGenericReturnType());
        this.genericType = GenericUtils.resolveTypeParameters(daoMetaData.getDaoClass(), method.getGenericReturnType());
        // 获取方法参数列表中定义的annotation
        Annotation[][] annotations = method.getParameterAnnotations();
        this.sqlParams = new SqlParam[annotations.length];
        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof SqlParam) {
                    this.sqlParams[index] = (SqlParam) annotation;
                }
            }
        }
    }

    private SqlType resolveSqlType(Sql sqlAnnotation) {
        SqlType sqlType = sqlAnnotation.type();

        if (sqlType != SqlType.AUTO_DETECT) {
            // 明确标注了sql type的
            return sqlType;
        }
        // 没有明确标注sql类型的,根据sql语句和dao方法名字判断
        if (SqlTypeUtils.matchQuery(sql, method.getName())) {
            sqlType = SqlType.READ;
        } else {
            sqlType = SqlType.WRITE;
        }

        return sqlType;
    }

    public SqlParam[] getSqlParams() {
        return sqlParams;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public Class getReturnType() {
        return returnType;
    }

    public Method getMethod() {
        return method;
    }

    public String getSql() {
        return sql;
    }

    public Class[] getGenericReturnTypes() {
        return genericType;
    }
}
