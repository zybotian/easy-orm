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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        // 转换基本类型
        Class<?> returnType = metaData.getReturnType();
        if (returnType.isPrimitive()) {
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
            default: {
                return 0;
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
}
