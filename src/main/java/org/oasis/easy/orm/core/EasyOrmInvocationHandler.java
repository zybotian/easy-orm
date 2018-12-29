package org.oasis.easy.orm.core;

import org.oasis.easy.orm.annotations.SqlParam;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.statement.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmInvocationHandler implements InvocationHandler {

    /**
     * 暂时只支持最多32个参数
     */
    private static final String[] INDEX_NAMES = new String[]{":1", ":2", ":3", ":4", ":5", ":6",
            ":7", ":8", ":9", ":10", ":11", ":12", ":13", ":14", ":15", ":16", ":17", ":18", ":19",
            ":20", ":21", ":22", ":23", ":24", ":25", ":26", ":27", ":28", ":29", ":30", ":31", ":32"};

    private final DaoMetadata daoMetaData;

    public EasyOrmInvocationHandler(DaoMetadata daoMetaData) {
        this.daoMetaData = daoMetaData;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return invokeObjectMethod(proxy, method, args);
        }

        // 获取statement
        Statement statement = getStatement(method);

        // 拼装参数
        Map<String, Object> parameters = new HashMap<>();
        StatementMetadata statementMetaData = statement.getMetadata();
        if (!ObjectUtils.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                parameters.put(INDEX_NAMES[i], args[i]);
                SqlParam sqlParam = statementMetaData.getSqlParams()[i];
                if (sqlParam != null) {
                    parameters.put(sqlParam.value(), args[i]);
                }
            }
        }

        // 执行sql,返回执行结果
        return statement.execute(parameters);
    }

    private Statement getStatement(Method method) {
        return new JdbcStatement();
    }

    private Object invokeObjectMethod(Object proxy, Method method, Object[] args) {
        if (ReflectionUtils.isToStringMethod(method)) {
            return EasyOrmInvocationHandler.class.toString();
        }
        if (ReflectionUtils.isHashCodeMethod(method)) {
            return daoMetaData.getDaoClass().hashCode() * 13 + this.hashCode();
        }
        if (ReflectionUtils.isEqualsMethod(method)) {
            return args[0] == proxy;
        }
        throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, daoMetaData.getDaoClass().getName()
                + "#" + method.getName());
    }
}
