package org.oasis.easy.orm.utils;

import com.google.common.collect.ImmutableMap;

import org.oasis.easy.orm.annotations.*;
import org.oasis.easy.orm.constant.Operator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-09
 */
public class OperatorUtils {
    private static final Map<Class<? extends Annotation>, Operator> ANNOTATION_OPERATOR_MAP;
    private static final Map<Operator, String> OPERATOR_VALUE_MAP;

    static {
        Map<Class<? extends Annotation>, Operator> operators = new HashMap<>(Operator.values().length);

        operators.put(Like.class, Operator.LIKE);
        operators.put(In.class, Operator.IN);

        operators.put(Offset.class, Operator.OFFSET);
        operators.put(Limit.class, Operator.LIMIT);

        operators.put(Eq.class, Operator.EQ);
        operators.put(Ne.class, Operator.NE);
        operators.put(Ge.class, Operator.GE);
        operators.put(Gt.class, Operator.GT);
        operators.put(Le.class, Operator.LE);
        operators.put(Lt.class, Operator.LT);

        ANNOTATION_OPERATOR_MAP = ImmutableMap.<Class<? extends Annotation>, Operator>builder().putAll(operators).build();
    }

    static {
        Map<Operator, String> operators = new HashMap<>(Operator.values().length);
        operators.put(Operator.EQ, " = ");
        operators.put(Operator.NE, " != ");
        operators.put(Operator.GE, " >= ");
        operators.put(Operator.GT, " > ");
        operators.put(Operator.LE, " <= ");
        operators.put(Operator.LT, " < ");
        operators.put(Operator.LIKE, " LIKE ");
        operators.put(Operator.IN, " IN ");
        OPERATOR_VALUE_MAP = ImmutableMap.<Operator, String>builder().putAll(operators).build();
    }

    public static final Map<Class<? extends Annotation>, Operator> getAnnotationOperationMap() {
        return ANNOTATION_OPERATOR_MAP;
    }

    public static final Map<Operator, String> getOperatorValueMap() {
        return OPERATOR_VALUE_MAP;
    }
}
