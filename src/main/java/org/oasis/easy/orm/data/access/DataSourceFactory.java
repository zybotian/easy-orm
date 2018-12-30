package org.oasis.easy.orm.data.access;

import javax.sql.DataSource;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface DataSourceFactory {
    DataSource getDataSource();
}
