package org.oasis.easy.orm.data.access;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class DataAccessFactoryImpl implements DataAccessFactory {

    private final ListableBeanFactory beanFactory;

    public DataAccessFactoryImpl(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public DataAccess getDataAccess() {
        return null;
    }
}
