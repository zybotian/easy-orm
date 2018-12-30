package org.oasis.easy.orm.interpreter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.Order;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class InterpreterFactoryImpl implements InterpreterFactory {
    private final ListableBeanFactory beanFactory;

    private List<Interpreter> interpreters;

    public InterpreterFactoryImpl(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public List<Interpreter> getInterpreters() {
        if (CollectionUtils.isNotEmpty(interpreters)) {
            return interpreters;
        }
        synchronized (InterpreterFactoryImpl.class) {
            if (CollectionUtils.isEmpty(interpreters)) {
                Map<String, Interpreter> interpreterMap = beanFactory.getBeansOfType(Interpreter.class);
                if (MapUtils.isNotEmpty(interpreterMap)) {
                    interpreters = new ArrayList<>(interpreterMap.values());
                }

                if (CollectionUtils.isEmpty(interpreters)) {
                    interpreters = new ArrayList<>();
                }

                // 检查配置中是否有SystemInterpreter
                boolean hasDefault = false;
                for (Interpreter interpreter : interpreters) {
                    if (interpreter instanceof SystemInterpreter) {
                        hasDefault = true;
                        break;
                    }
                }
                if (!hasDefault) {
                    // xml中没有配置SystemInterpreter,则添加
                    interpreters.add(new SystemInterpreter());
                }

                Collections.sort(interpreters, new Comparator<Interpreter>() {
                    @Override
                    public int compare(Interpreter o1, Interpreter o2) {
                        Order order1 = o1.getClass().getAnnotation(Order.class);
                        Order order2 = o2.getClass().getAnnotation(Order.class);
                        // 没有标注@Order的,设置为INTEGER最大值,表示优先级最低
                        int order1Value = order1 == null ? Integer.MAX_VALUE : order1.value();
                        int order2Value = order2 == null ? Integer.MAX_VALUE : order2.value();
                        return order1Value - order2Value;
                    }
                });
            }
        }
        return interpreters;
    }
}
