package org.oasis.easy.orm.interpreter;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class InterpreterFactoryImpl implements InterpreterFactory {
    private final ListableBeanFactory beanFactory;

    public InterpreterFactoryImpl(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public List<Interpreter> getInterpreters() {
        return null;
    }
}
