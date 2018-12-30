package org.oasis.easy.orm.converter;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public abstract class MapConverter implements ResultConverter {

    @Override
    public Object convert(List<?> listResult, Class<?> returnType) {
        Map map = creatMap(returnType);
        for (Object obj : listResult) {
            if (obj == null) {
                continue;
            }

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;

            if (map.getClass() == Hashtable.class && entry.getKey() == null) {
                continue;
            }

            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    protected abstract Map creatMap(Class<?> returnType);

}