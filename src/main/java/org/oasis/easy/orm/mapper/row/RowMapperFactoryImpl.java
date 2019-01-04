package org.oasis.easy.orm.mapper.row;

import org.apache.commons.lang3.ClassUtils;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.statement.StatementMetadata;
import org.oasis.easy.orm.utils.TypeUtils;
import org.springframework.jdbc.core.*;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class RowMapperFactoryImpl implements RowMapperFactory {

    // 获得返回的集合元素类型
    private static Class<?> getRowType(StatementMetadata statementMetaData) {
        Class<?> returnClassType = statementMetaData.getReturnType();
        if (Collection.class.isAssignableFrom(returnClassType)
                || Iterable.class == returnClassType
                || Iterator.class == returnClassType) {
            return getRowTypeFromCollectionType(statementMetaData, returnClassType);
        } else if (Map.class.isAssignableFrom(returnClassType)) {
            return getRowTypeFromMapType(statementMetaData, returnClassType);
        } else if (returnClassType.isArray() && returnClassType != byte[].class) {
            return returnClassType.getComponentType();
        }

        return returnClassType;
    }

    private static Class<?> getRowTypeFromMapType(StatementMetadata modifier,
                                                  Class<?> returnClassType) {
        Class<?> rowType;
        // 获取Map<K, V>值元素类型
        Class<?>[] genericTypes = modifier.getGenericReturnTypes();
        if (genericTypes.length != 2) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM,
                    "return generic type " + returnClassType.getName() + " should has two actual type parameters");
        }

        // 取V类型
        rowType = genericTypes[1];
        return rowType;
    }

    private static Class<?> getRowTypeFromCollectionType(StatementMetadata modifier,
                                                         Class<?> returnClassType) {
        Class<?> rowType;
        // 仅支持  List/ArrayList/LinkedList, Collection/Iterable/Iterator, Set/HashSet
        if ((returnClassType != List.class) && (returnClassType != ArrayList.class)
                && (returnClassType != LinkedList.class)
                && (returnClassType != Collection.class) && (returnClassType != Iterable.class)
                && (returnClassType != Iterator.class) && (returnClassType != Set.class)
                && (returnClassType != HashSet.class)) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM,
                    "error collection type " + returnClassType.getName());
        }
        // 获取集合元素类型
        Class<?>[] genericTypes = modifier.getGenericReturnTypes();
        if (genericTypes.length != 1) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM,
                    "return generic type " + returnClassType.getName() + " should has a actual type parameter");
        }
        rowType = genericTypes[0];
        return rowType;
    }

    @Override
    public RowMapper<?> getRowMapper(StatementMetadata smd) {

        Class<?> rowType = getRowType(smd);

        if (rowType.isPrimitive()) {
            rowType = ClassUtils.primitiveToWrapper(rowType);
        }

        if (TypeUtils.isValidColumnType(rowType)) {
            return new SingleColumnRowMapper(rowType);
        }
        RowMapper rowMapper;
        if (rowType == Map.class) {
            rowMapper = new ColumnMapRowMapper();
        } else if (rowType.isArray()) {
            rowMapper = new ArrayRowMapper(rowType);
        } else if ((rowType == List.class) || (rowType == Collection.class)) {
            rowMapper = new ListRowMapper(smd);
        } else if (rowType == Set.class) {
            rowMapper = new SetRowMapper(smd);
        } else {
            rowMapper = new BeanPropertyRowMapper(rowType);
        }

        return rowMapper;
    }
}
