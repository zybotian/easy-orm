package org.oasis.easy.orm.converter;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class MapAssignableResultConverter extends MapConverter {

    @Override
    protected Map creatMap(Class<?> returnType) {
        try {
            return (Map) returnType.newInstance();
        } catch (Exception ex) {
            throw new EasyOrmException(ErrorCode.OBJECT_CREATING_ERROR, "error creating instance of " + returnType.getName());
        }
    }
}