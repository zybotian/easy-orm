package org.oasis.easy.orm.statement;

import org.oasis.easy.orm.data.access.DataAccessFactory;
import org.oasis.easy.orm.interpreter.InterpreterFactory;
import org.oasis.easy.orm.mapper.row.RowMapperFactory;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class DaoConfig {
    private DataAccessFactory dataAccessFactory;

    private InterpreterFactory interpreterFactory;

    private RowMapperFactory rowMapperFactory;

    public void setDataAccessFactory(DataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = dataAccessFactory;
    }

    public void setInterpreterFactory(InterpreterFactory interpreterFactory) {
        this.interpreterFactory = interpreterFactory;
    }

    public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }
}
