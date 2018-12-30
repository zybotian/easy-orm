package org.oasis.easy.orm.converter;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class ArrayTypePrimitiveResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        int size = CollectionUtils.size(listResult);
        Object array = Array.newInstance(returnType.getComponentType(), size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, listResult.get(i));
        }
        return array;
    }
}