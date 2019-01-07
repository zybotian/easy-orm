package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.utils.GenericUtils;

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

    public DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public Class resolveTypeVariable(Class<?> declaredClass, String typeVarName) {
        return GenericUtils.resolveTypeVariable(daoClass, declaredClass, typeVarName);
    }
}
