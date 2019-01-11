package org.oasis.easy.orm.statement;

import org.apache.commons.lang3.ClassUtils;
import org.oasis.easy.orm.annotations.Batchable;
import org.oasis.easy.orm.annotations.ReturnGeneratedKeys;
import org.oasis.easy.orm.constant.SqlType;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.interpreter.Interpreter;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class JdbcStatement implements Statement {

    private final StatementMetadata metadata;

    private final List<Interpreter> interpreters;

    private final Querier querier;

    private final SqlType sqlType;

    private final boolean batchUpdate;

    public JdbcStatement(StatementMetadata metadata,
                         SqlType sqlType,
                         List<Interpreter> interpreters,
                         Querier querier) {
        this.metadata = metadata;
        this.interpreters = interpreters == null ? new ArrayList<Interpreter>() : interpreters;
        this.sqlType = sqlType;
        this.querier = querier;
        if (sqlType == SqlType.WRITE) {
            Method method = metadata.getMethod();
            if (method.isAnnotationPresent(Batchable.class)) {
                // 方法上明确标记了@Batchable的,认为是批量写操作
                this.batchUpdate = true;
            } else {
                this.batchUpdate = false;
            }

            Class<?> returnType = metadata.getReturnType();
            if (returnType.isPrimitive()) {
                returnType = ClassUtils.primitiveToWrapper(returnType);
            }

            ReturnGeneratedKeys returnGeneratedKeys = metadata.getMethod().getAnnotation(ReturnGeneratedKeys.class);
            if (this.batchUpdate) {
                if (returnGeneratedKeys != null) {
                    throw new EasyOrmException(ErrorCode.INCORRECT_DATA_TYPE_ERROR,
                            "batch update method cannot return generated keys: " + method);
                }
                if (returnType != void.class && returnType != int[].class
                        && returnType != Integer.class && returnType != Boolean.class) {
                    throw new EasyOrmException(ErrorCode.INCORRECT_DATA_TYPE_ERROR,
                            "return type only support type of {void,boolean,int,int[]}: " + method);
                }
            } else {
                if (returnGeneratedKeys != null) {
                    if (!Number.class.isAssignableFrom(returnType)) {
                        throw new EasyOrmException(ErrorCode.INCORRECT_DATA_TYPE_ERROR,
                                "return type only support number: " + method);
                    }
                } else if (returnType != void.class && returnType != Boolean.class && returnType != Integer.class) {
                    throw new EasyOrmException(ErrorCode.INCORRECT_DATA_TYPE_ERROR,
                            "return type only support {void,boolean,int}: " + method);
                }
            }
        } else {
            this.batchUpdate = false;
        }
    }

    @Override
    public StatementMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Object execute(Map<String, Object> parameters) {
        if (!batchUpdate) {
            StatementRuntime runtime = new StatementRuntime(metadata, parameters);
            for (Interpreter interpreter : interpreters) {
                interpreter.interpret(runtime);
            }
            return querier.execute(sqlType, runtime);
        }

        List<StatementRuntime> runtimes = new LinkedList<>();
        Iterable<?> iterable = (Iterable<?>) parameters.get(":1");
        Iterator<?> iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Object arg = iterator.next();
            Map<String, Object> args = new HashMap<>(2);
            args.put(":1", arg);

            StatementRuntime runtime = new StatementRuntime(metadata, args);
            for (Interpreter interpreter : interpreters) {
                interpreter.interpret(runtime);
            }
            runtimes.add(runtime);
        }

        return querier.execute(sqlType, runtimes.toArray(new StatementRuntime[runtimes.size()]));
    }
}
