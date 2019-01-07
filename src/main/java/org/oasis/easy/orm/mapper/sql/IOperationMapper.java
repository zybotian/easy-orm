package org.oasis.easy.orm.mapper.sql;

import org.oasis.easy.orm.mapper.sql.impl.EntityMapperFactory;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public interface IOperationMapper {
    String OPERATION_SELECT = "SELECT";
    String OPERATION_INSERT = "INSERT";
    String OPERATION_DELETE = "DELETE";
    String OPERATION_UPDATE = "UPDATE";

    String[] OPERATION_KEYS = {
            OPERATION_SELECT,
            OPERATION_INSERT,
            OPERATION_DELETE,
            OPERATION_UPDATE
    };

    String[] OPERATION_PREFIX_SELECT = {"get", "find", "query", "count"};
    String[] OPERATION_PREFIX_INSERT = {"save", "insert"};
    String[] OPERATION_PREFIX_DELETE = {"delete", "remove"};
    String[] OPERATION_PREFIX_UPDATE = {"update", "modify"};

    String[][] OPERATION_PREFIX = {
            OPERATION_PREFIX_SELECT,
            OPERATION_PREFIX_INSERT,
            OPERATION_PREFIX_DELETE,
            OPERATION_PREFIX_UPDATE
    };

    String getOperationName();

    IEntityMapper getEntityMapper();

    void setEntityMapperFactory(EntityMapperFactory entityMapperFactory);

    boolean isPrimaryKeyMode();
}
