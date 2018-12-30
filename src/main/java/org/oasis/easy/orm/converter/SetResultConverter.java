package org.oasis.easy.orm.converter;

import java.util.HashSet;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class SetResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        return new HashSet<>(listResult);
    }
}