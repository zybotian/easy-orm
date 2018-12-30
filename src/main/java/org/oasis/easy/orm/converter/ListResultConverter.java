package org.oasis.easy.orm.converter;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class ListResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        return listResult;
    }
}