package org.oasis.easy.orm.data.access;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class DataAccessFactoryImpl implements DataAccessFactory {

    private DataSourceFactory dataSourceFactory;

    private ConcurrentHashMap<DataSource, DataAccess> dataAccessCache = new ConcurrentHashMap<>();

    public DataAccessFactoryImpl(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public DataAccess getDataAccess() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        if (dataSource == null) {
            throw new EasyOrmException(ErrorCode.SERVICE_ERROR, "can not find a data source");
        }
        DataAccess dataAccess = dataAccessCache.get(dataSource);
        if (dataAccess == null) {
            dataAccess = new DataAccessImpl(dataSource);
            dataAccessCache.put(dataSource, dataAccess);
        }
        return dataAccess;
    }
}
