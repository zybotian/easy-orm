package org.oasis.easy.orm.data.access;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.sql.DataSource;

/**
 * @author Paul
 * @date 2018-12-29
 */
public class DataSourceFactoryImpl implements DataSourceFactory {
    private ListableBeanFactory applicationContext;

    private DataSource dataSource;

    public DataSourceFactoryImpl(ListableBeanFactory beanFactory) {
        this.applicationContext = beanFactory;
    }

    @Override
    public DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }

        dataSource = getDataSourceByKey("easy.orm.dataSource");
        if (dataSource != null) {
            return dataSource;
        }

        dataSource = getDataSourceByKey("dataSource");
        if (dataSource != null) {
            return dataSource;
        }
        throw new EasyOrmException(ErrorCode.CONFIG_ERROR, "missing data source config");
    }

    private DataSource getDataSourceByKey(String key) {
        if (applicationContext.containsBean(key)) {
            Object dataSource = applicationContext.getBean(key);
            if (dataSource instanceof DataSource) {
                return (DataSource) dataSource;
            }
            throw new EasyOrmException(ErrorCode.CONFIG_ERROR, "expects DataSource, but a " + dataSource.getClass().getName());
        }
        return null;
    }
}
