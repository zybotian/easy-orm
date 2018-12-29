package org.oasis.easy.orm.core;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.statement.DaoMetadata;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmInvocationHandler implements InvocationHandler {

    private final DaoMetadata daoMetaData;

    public EasyOrmInvocationHandler(DaoMetadata daoMetaData) {
        this.daoMetaData = daoMetaData;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return invokeObjectMethod(proxy, method, args);
        }

        return args[0];
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
