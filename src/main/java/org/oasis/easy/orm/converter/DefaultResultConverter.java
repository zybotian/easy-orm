package org.oasis.easy.orm.converter;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class DefaultResultConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        final int sizeResult = listResult.size();
        if (sizeResult == 1) {
            // 返回单个Bean、Boolean等类型对象
            return listResult.get(0);
        } else if (sizeResult == 0) {
            if (returnType.isPrimitive()) {
                // 基础类型的抛异常
                throw new EasyOrmException(ErrorCode.EMPTY_DATA_SET_ERROR);
            } else {
                // 对象类型的返回null
                return null;
            }
        } else {
            throw new EasyOrmException(ErrorCode.INCORRECT_DATA_SIZE_ERROR);
        }
    }
}
