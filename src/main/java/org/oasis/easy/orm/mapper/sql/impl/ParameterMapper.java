package org.oasis.easy.orm.mapper.sql.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.oasis.easy.orm.constant.Operator;
import org.oasis.easy.orm.mapper.sql.IColumnMapper;
import org.oasis.easy.orm.mapper.sql.IParameterMapper;
import org.oasis.easy.orm.utils.OperatorUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-09
 */
public class ParameterMapper implements IParameterMapper {

    private static final Map<Class<? extends Annotation>, Operator> OPERATORS = OperatorUtils.getAnnotationOperationMap();

    private final MethodParameter methodParameter;

    private Operator operator;

    private IColumnMapper columnMapper;

    public ParameterMapper(MethodParameter methodParameter, IColumnMapper columnMapper) {
        this.methodParameter = methodParameter;
        this.columnMapper = columnMapper;
        this.operator = setOperator();
    }

    /**
     * 遍历方法参数的注解数组,获取参数的操作符号,以第一个获取到的操作符为准,若没有操作符注解,默认为EQ
     */
    private Operator setOperator() {
        Annotation[] annotations = this.methodParameter.getAnnotations();
        if (ArrayUtils.isEmpty(annotations)) {
            return Operator.EQ;
        }
        for (Annotation annotation : annotations) {
            Operator operator = OPERATORS.get(annotation.annotationType());
            if (operator != null) {
                return operator;
            }
        }
        return Operator.EQ;
    }

    @Override
    public Operator getOperator() {
        return this.operator;
    }

    @Override
    public String getParameterName() {
        return methodParameter.getParameterName();
    }

    @Override
    public IColumnMapper getColumnMapper() {
        return columnMapper;
    }

    @Override
    public Class<?> getType() {
        return methodParameter.getType();
    }
}
