package org.oasis.easy.orm.utils;

import org.springframework.util.ClassUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class TypeUtils {

    public static boolean isValidColumnType(Class<?> clazz) {
        return String.class == clazz
                || Date.class == clazz
                || byte[].class == clazz
                || BigDecimal.class == clazz
                || Blob.class == clazz
                || Clob.class == clazz
                || ClassUtils.isPrimitiveOrWrapper(clazz);
    }
}
