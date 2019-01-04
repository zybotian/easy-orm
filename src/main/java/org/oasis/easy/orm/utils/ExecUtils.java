package org.oasis.easy.orm.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExecUtils {

    public static Collection asCollection(Object obj) {
        if (obj == null) {
            return Collections.EMPTY_SET;
        }

        if (obj.getClass().isArray()) {
            return Lists.newArrayList(asArray(obj));
        }

        if (obj instanceof Collection) {
            return (Collection) obj;
        }

        if (obj instanceof Map) {
            return ((Map) obj).entrySet();
        }

        return Lists.newArrayList(obj);
    }

    public static Object[] asArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().isPrimitive()) {
                int length = Array.getLength(obj);
                Object[] result = new Object[length];
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(obj, i);
                    result[i] = o;
                }
                return result;
            } else {
                return (Object[]) obj;
            }
        }
        return new Object[]{obj};
    }

    public static boolean isValid(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() > 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size() > 0;
        } else if (obj instanceof Map) {
            return ((Map) obj).size() > 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) > 0;
        }

        return true;
    }
}
