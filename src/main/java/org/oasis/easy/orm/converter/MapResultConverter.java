package org.oasis.easy.orm.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
@Converter
public class MapResultConverter extends MapConverter {

    @Override
    protected Map creatMap(Class<?> returnType) {
        return new HashMap<>();
    }
}