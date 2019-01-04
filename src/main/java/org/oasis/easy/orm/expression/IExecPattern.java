package org.oasis.easy.orm.expression;

import org.oasis.easy.orm.exception.EasyOrmException;

import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public interface IExecPattern {
    void execute(IExecContext context, Map<String, ?> map) throws EasyOrmException;
}
