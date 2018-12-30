package org.oasis.easy.orm.converter;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.reflections.Reflections;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class ResultConverterFactory {

    private static final Map<String, ResultConverter> converterMap = new HashMap<>();

    private static final Map<Class<?>, String> typeNameMap = new HashMap<>();

    static {
        initConverterMap();
        initTypeNameMap();
    }

    private static void initConverterMap() {
        String currentPackageName = ResultConverterFactory.class.getPackage().getName();
        Reflections reflections = new Reflections(currentPackageName);
        Set<Class<?>> converterTypes = reflections.getTypesAnnotatedWith(Converter.class);
        try {
            for (Class<?> clazz : converterTypes) {
                converterMap.put(clazz.getName(), (ResultConverter) clazz.newInstance());
            }
        } catch (Exception ex) {
            throw new EasyOrmException(ErrorCode.SERVICE_ERROR, "init converter map failed");
        }
    }

    private static void initTypeNameMap() {
        // List/Collection/Iterable -> ListResultConverter
        typeNameMap.put(List.class, ListResultConverter.class.getName());
        typeNameMap.put(Collection.class, ListResultConverter.class.getName());
        typeNameMap.put(Iterable.class, ListResultConverter.class.getName());

        // ArrayList ->  ArrayListResultConverter
        typeNameMap.put(ArrayList.class, ArrayListResultConverter.class.getName());

        // LinkedList -> LinkedListResultConverter
        typeNameMap.put(LinkedList.class, LinkedListResultConverter.class.getName());

        // Set/HashSet -> SetResultConverter
        typeNameMap.put(Set.class, SetResultConverter.class.getName());
        typeNameMap.put(HashSet.class, SetResultConverter.class.getName());

        // Iterator -> IteratorResultConverter
        typeNameMap.put(Iterator.class, IteratorResultConverter.class.getName());

        // Map/HashMap -> MapResultConverter
        typeNameMap.put(Map.class, MapResultConverter.class.getName());
        typeNameMap.put(HashMap.class, MapResultConverter.class.getName());

        // Hashtable -> HashtableResultConverter
        typeNameMap.put(Hashtable.class, HashTableResultConverter.class.getName());
    }

    /**
     * 能直接找到的,直接返回
     */
    public static ResultConverter getInstance(Class<?> clazz) {
        return converterMap.get(typeNameMap.get(clazz));
    }

    /**
     * 不能直接找到的,通过反射来创建
     */
    public static ResultConverter newInstance(Class clazz) {
        try {
            return (ResultConverter) clazz.newInstance();
        } catch (Exception ex) {
            throw new EasyOrmException(ErrorCode.OBJECT_CREATING_ERROR, "create result converter failed:" + clazz);
        }
    }
}
