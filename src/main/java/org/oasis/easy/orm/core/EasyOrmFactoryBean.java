package org.oasis.easy.orm.core;

import org.oasis.easy.orm.data.access.DataAccessFactory;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.interpreter.InterpreterFactory;
import org.oasis.easy.orm.mapper.row.RowMapperFactory;
import org.oasis.easy.orm.statement.DaoConfig;
import org.oasis.easy.orm.statement.DaoMetadata;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmFactoryBean implements FactoryBean {

    private Class<?> objectType;

    private Object targetProxy;

    private DataAccessFactory dataAccessFactory;

    private InterpreterFactory interpreterFactory;

    private RowMapperFactory rowMapperFactory;

    @Override
    public Object getObject() throws Exception {
        if (targetProxy == null) {
            targetProxy = createTargetProxy();
        }
        return targetProxy;
    }

    private Object createTargetProxy() {
        try {
            DaoConfig daoConfig = new DaoConfig();
            daoConfig.setDataAccessFactory(dataAccessFactory);
            daoConfig.setInterpreterFactory(interpreterFactory);
            daoConfig.setRowMapperFactory(rowMapperFactory);
            DaoMetadata daoMetaData = new DaoMetadata(objectType, daoConfig);
            // 通过JDK动态代理创建dao的代理类
            return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[]{objectType},
                    new EasyOrmInvocationHandler(daoMetaData));
        } catch (Exception ex) {
            throw new EasyOrmException(ErrorCode.OBJECT_CREATING_ERROR, "failed to create bean for " + objectType.getName());
        }
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    // Spring创建对象时调用
    public void setObjectType(Class<?> objectType) {
        this.objectType = objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // Spring创建对象时调用
    public void setDataAccessFactory(DataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = dataAccessFactory;
    }

    // Spring创建对象时调用
    public void setInterpreterFactory(InterpreterFactory interpreterFactory) {
        this.interpreterFactory = interpreterFactory;
    }

    // Spring创建对象时调用
    public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }
}
