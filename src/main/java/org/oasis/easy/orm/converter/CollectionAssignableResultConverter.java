package org.oasis.easy.orm.converter;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.util.Collection;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class CollectionAssignableResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        Collection listToReturn;
        try {
            listToReturn = (Collection) returnType.newInstance();
        } catch (Exception ex) {
            throw new EasyOrmException(ErrorCode.OBJECT_CREATING_ERROR, "error to create instance of " + returnType.getName());
        }
        listToReturn.addAll(listResult);
        return listToReturn;
    }
}