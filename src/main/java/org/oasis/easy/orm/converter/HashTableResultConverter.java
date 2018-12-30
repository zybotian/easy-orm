package org.oasis.easy.orm.converter;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class HashTableResultConverter extends MapConverter {
    @Override
    protected Map creatMap(Class<?> returnType) {
        return new Hashtable();
    }
}
