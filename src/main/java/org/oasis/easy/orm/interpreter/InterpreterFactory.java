package org.oasis.easy.orm.interpreter;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface InterpreterFactory {
    List<Interpreter> getInterpreters();
}
