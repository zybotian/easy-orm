package org.oasis.easy.orm.statement;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class DaoMetadata {
    private DaoConfig daoConfig;

    private Class<?> daoClass;

    public DaoMetadata(Class<?> daoClass, DaoConfig daoConfig) {
        this.daoClass = daoClass;
        this.daoConfig = daoConfig;
    }

    public Class<?> getDaoClass() {
        return daoClass;
    }
}
