package org.oasis.easy.orm.converter;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public interface ResultConverter {
    Object convert(List<?> listResult, Class<?> returnType);
}
