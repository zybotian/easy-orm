package org.oasis.easy.orm.statement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.oasis.easy.orm.annotations.ReturnGeneratedKeys;
import org.oasis.easy.orm.constant.SqlType;
import org.oasis.easy.orm.data.access.DataAccess;
import org.oasis.easy.orm.data.access.DataAccessFactory;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-30
 */
public class UpdateQuerier implements Querier {

    private final DataAccessFactory dataAccessFactory;

    private final Class<?> returnType;

    private final boolean returnGeneratedKeys;

    public UpdateQuerier(DataAccessFactory dataAccessFactory, StatementMetadata metaData) {
        this.dataAccessFactory = dataAccessFactory;
        Method method = metaData.getMethod();
        Class<?> returnType = metaData.getReturnType();
        if (returnType.isPrimitive()) {
            // 转换基本类型
            returnType = ClassUtils.primitiveToWrapper(returnType);
        }
        this.returnType = returnType;
        this.returnGeneratedKeys = method.isAnnotationPresent(ReturnGeneratedKeys.class);
    }

    @Override
    public Object execute(SqlType sqlType, StatementRuntime... runtimes) {
        switch (runtimes.length) {
            case 1: {
                return executeSingle(runtimes[0]);
            }
            case 0: {
                return 0;
            }
            default: {
                return executeBatch(runtimes);
            }
        }
    }

    private Object executeSingle(StatementRuntime runtime) {
        Number result;
        DataAccess dataAccess = dataAccessFactory.getDataAccess();
        if (returnGeneratedKeys) {
            List<Map<String, Object>> keys = new ArrayList<>();
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder(keys);
            dataAccess.update(runtime.getSql(), runtime.getArgs(), generatedKeyHolder);
            if (CollectionUtils.isNotEmpty(keys)) {
                result = generatedKeyHolder.getKey();
            } else {
                result = null;
            }
        } else {
            result = dataAccess.update(runtime.getSql(), runtime.getArgs(), null);
        }

        if (result == null || returnType == void.class) {
            return null;
        }
        if (returnType == result.getClass()) {
            return result;
        }
        return getNumberResult(result);
    }

    private Object getNumberResult(Number result) {
        if (returnType == Integer.class) {
            return result.intValue();
        } else if (returnType == Long.class) {
            return result.longValue();
        } else if (returnType == Boolean.class) {
            return result.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
        } else if (returnType == Double.class) {
            return result.doubleValue();
        } else if (returnType == Float.class) {
            return result.floatValue();
        } else if (returnType == Number.class) {
            return result;
        } else {
            throw new EasyOrmException(ErrorCode.INCORRECT_DATA_TYPE_ERROR, "generated key is not a supported numeric type: " + returnType.getName());
        }
    }

    private Object executeBatch(StatementRuntime[] runtimes) {
        List<Object[]> args = new ArrayList<>();
        for (StatementRuntime runtime : runtimes) {
            args.add(runtime.getArgs());
        }
        DataAccess dataAccess = dataAccessFactory.getDataAccess();
        String sql = runtimes[0].getSql();
        int[] batchUpdate = dataAccess.batchUpdate(sql, args);
        if (returnType == void.class) {
            return null;
        }
        if (returnType == int[].class) {
            return batchUpdate;
        }
        if (returnType == Integer.class || returnType == Boolean.class) {
            int updated = 1;
            for (int value : batchUpdate) {
                if (value < 1) {
                    updated = 0;
                    break;
                }
            }
            return returnType == Boolean.class ? updated > 0 : updated;
        }
        throw new EasyOrmException(ErrorCode.INVALID_PARAM,
                "bad return type for batch update: " + runtimes[0].getMetadata().getMethod());
    }
}
