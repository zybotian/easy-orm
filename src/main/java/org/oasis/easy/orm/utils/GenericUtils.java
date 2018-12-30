package org.oasis.easy.orm.utils;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class GenericUtils {
    private static final Class[] EMPTY_CLASSES = new Class[0];

    /**
     * 返回泛型的容器类型
     * @param invocationClass
     * @param targetType
     * @return
     */
    public static Class[] resolveTypeParameters(Class invocationClass, Type targetType) {
        if (targetType instanceof ParameterizedType) {
            // 带泛型的类型
            Type[] actualTypes = ((ParameterizedType) targetType).getActualTypeArguments();
            Class[] actualClasses = new Class[actualTypes.length];
            for (int i = 0; i < actualTypes.length; i++) {
                actualClasses[i] = resolveTypeVariable(invocationClass, actualTypes[i]);
            }
            return actualClasses;
        }

        return EMPTY_CLASSES;
    }

    /**
     * 返回泛型的类型
     * @param invocationClass
     * @param targetType
     * @return
     */
    public static Class resolveTypeVariable(Class invocationClass, Type targetType) {
        if (targetType == null) {
            throw new EasyOrmException(ErrorCode.MISSING_CONFIG, "type variable is null");
        }
        // Class类型
        if (targetType instanceof Class) {
            return (Class) targetType;
        }
        // 带参数的容器类型, 非具体的参数类型，即List<Integer>返回List，而不是Integer
        if (targetType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) targetType).getRawType();
            return resolveTypeVariable(invocationClass, rawType);
        }
        // 数组类型
        if (targetType instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) targetType).getGenericComponentType();
            componentType = resolveTypeVariable(invocationClass, componentType);
            return Array.newInstance((Class) componentType, 0).getClass();
        }

        // E-> UserDO, ID->Long等的映射
        Map<TypeVariable, Type> genericAndActualMap = new HashMap<>();

        List<Type> allSuperTypes = new LinkedList<>();
        allSuperTypes.addAll(Arrays.asList(invocationClass.getGenericInterfaces()));

        for (int i = 0; i < allSuperTypes.size(); i++) {
            Type type = allSuperTypes.get(i);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = ((ParameterizedType) type);
                Class interfaceClass = (Class) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable[] typeParameters = interfaceClass.getTypeParameters();

                if (actualTypeArguments.length != typeParameters.length) {
                    throw new EasyOrmException(ErrorCode.INVALID_PARAM, "actual type arguments not matching with type" +
                            " parameters");
                }

                for (int index = 0; index < actualTypeArguments.length; index++) {
                    genericAndActualMap.put(typeParameters[index], actualTypeArguments[index]);
                }
            }
        }

        Type returnType = targetType;
        Type originType = returnType;
        returnType = genericAndActualMap.get(returnType);
        if (returnType instanceof Class) {
            return (Class) returnType;
        }
        if (returnType == null) {
            returnType = originType;
            if (returnType instanceof WildcardType) {
                return (Class) ((WildcardType) returnType).getUpperBounds()[0];
            }
        }

        return (Class) ((TypeVariable) returnType).getBounds()[0];
    }
}
