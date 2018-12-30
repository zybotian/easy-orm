package org.oasis.easy.orm.converter;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class ArrayTypeNonePrimitiveResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        Object array = Array.newInstance(returnType.getComponentType(), CollectionUtils.size(listResult));
        return listResult.toArray((Object[]) array);
    }
}