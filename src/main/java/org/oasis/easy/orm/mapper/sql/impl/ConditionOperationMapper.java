package org.oasis.easy.orm.mapper.sql.impl;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.annotations.*;
import org.oasis.easy.orm.dao.BasicDao;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.*;
import org.oasis.easy.orm.statement.StatementMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class ConditionOperationMapper implements IOperationMapper {

    private static final long MODE_PRIMARY_KEY = 1;
    private static final long MODE_COMPLEX = 1 << 1;
    private static final long MODE_ENTITY = 1 << 2;
    private static final long MODE_ENTITY_COLLECTION = 1 << 3;
    private static final long MODE_LOCK = 1 << 4;
    private static final long MODE_INSERT_IGNORE = 1 << 5;

    private static final String ENTITY = "ENTITY";
    private static final String ID = "ID";

    private final StatementMetadata statementMetadata;

    private EntityMapperFactory entityMapperFactory;
    /**
     * 实体类的类型
     *
     * @see org.oasis.easy.orm.dao.BasicDao:ENTITY
     */
    private Class<?> entityType;
    /**
     * 实体类的主键id的类型
     *
     * @see org.oasis.easy.orm.dao.BasicDao:ID
     */
    private Class<?> primaryKeyType;

    private String operationName;

    private int whereAt = -1;

    private int offsetAt = -1;

    private int limitAt = -1;

    private long mode = 0;

    private List<IParameterMapper> parameterMappers;

    public ConditionOperationMapper(StatementMetadata statementMetadata, EntityMapperFactory entityMapperFactory) {
        this.statementMetadata = statementMetadata;
        this.entityMapperFactory = entityMapperFactory;
        this.entityType = statementMetadata.getDaoMetadata().resolveTypeVariable(BasicDao.class, ENTITY);
        this.primaryKeyType = statementMetadata.getDaoMetadata().resolveTypeVariable(BasicDao.class, ID);
        this.operationName = generateOperationName();
        // 注意:构造parameter map需要用到operation name,二者顺序切勿颠倒
        this.parameterMappers = generateParameterMappers(statementMetadata.getMethod());
        this.checkMode(statementMetadata.getMethod());
    }

    private void checkMode(Method method) {
        if (method.isAnnotationPresent(Lock.class)) {
            appendMode(MODE_LOCK);
        }
        if (StringUtils.equals(operationName, OPERATION_INSERT) && method.isAnnotationPresent(InsertIgnore.class)) {
            appendMode(MODE_INSERT_IGNORE);
        }
    }

    private String generateOperationName() {
        String methodName = statementMetadata.getMethod().getName();
        // 下标访问方式效率最高
        for (int i = 0; i < OPERATION_PREFIX.length; i++) {
            String[] prefixs = OPERATION_PREFIX[i];
            for (int j = 0; j < prefixs.length; j++) {
                // 按约定的单词开头
                if (StringUtils.startsWith(methodName, prefixs[j])) {
                    return OPERATION_KEYS[i];
                }
            }
        }
        throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unrecognized method " + methodName);
    }

    private List<IParameterMapper> generateParameterMappers(Method method) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (ArrayUtils.isEmpty(parameterTypes) && !StringUtils.equals(OPERATION_SELECT, operationName)) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM, "write method should have parameters");
        }

        List<IParameterMapper> parameterMapperList = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtils.isNotEmpty(parameterAnnotations)) {
            int parameterSize = parameterAnnotations.length;
            for (int i = 0; i < parameterSize; i++) {
                IParameterMapper parameterMapper = createParameterMapper(resolveParameterType(parameterTypes[i]),
                        parameterAnnotations[i], i);
                if (parameterMapper != null) {
                    parameterMapperList.add(parameterMapper);
                }
            }
        }
        return parameterMapperList;
    }

    private IParameterMapper createParameterMapper(Class<?> type, Annotation[] annotations, int index) {
        Method method = statementMetadata.getMethod();
        if (annotations.length == 0
                && primaryKeyType.isAssignableFrom(type)
                && method.getParameterTypes().length == 1) {
            appendMode(MODE_PRIMARY_KEY);
        } else if (entityType.isAssignableFrom(type) && method.getParameterTypes().length == 1) {
            if (Collection.class.isAssignableFrom(method.getParameterTypes()[index])) {
                appendMode(MODE_ENTITY_COLLECTION);
            } else {
                appendMode(MODE_ENTITY);
            }
        }

        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation.annotationType() == Offset.class) {

                if (!Number.class.isAssignableFrom(type)
                        && type != int.class
                        && type != long.class) {
                    throw new EasyOrmException(ErrorCode.MAPPING_ERROR, "offset must be a number");
                }
                offsetAt = index;
            } else if (annotation.annotationType() == Limit.class) {
                if (!Number.class.isAssignableFrom(type)
                        && type != int.class
                        && type != long.class) {
                    throw new EasyOrmException(ErrorCode.MAPPING_ERROR, "limit must be a number");
                }
                limitAt = index;
            } else if (annotation.annotationType() == Where.class
                    && OPERATION_UPDATE.equals(operationName)) {
                // Where条件的位置，用于更新操作，其他操作该注解无任何意义。
                if (index == 0) {
                    // 如果该注解被标记在第一个参数前，证明该操作没有任何值用于更新。
                    throw new EasyOrmException(ErrorCode.INVALID_PARAM, "@Where cannot appear before first parameter");
                }
                whereAt = index;
            }
            appendMode(MODE_COMPLEX);
        }

        SqlParam sqlParam = null;

        if (ArrayUtils.isNotEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == SqlParam.class) {
                    sqlParam = (SqlParam) annotation;
                    break;
                }
            }
        }

        MethodParameter methodParameter = new MethodParameter(sqlParam, type, annotations);
        IParameterMapper parameterMapper = new ParameterMapper(methodParameter, getEntityMapper()
                .getColumnMapperByFieldName(methodParameter.getParameterName()));
        return parameterMapper;
    }

    private Class<?> resolveParameterType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType
                && ((ParameterizedType) type).getRawType() instanceof Class
                && Collection.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType())) {
            // BasicDao中的集合泛型参数
            return resolveParameterType(((ParameterizedType) type).getActualTypeArguments()[0]);
        } else if (type instanceof TypeVariable) {
            // BasicDao中的非集合泛型参数，或许是主键或许是实体。
            GenericDeclaration genericDeclaration = ((TypeVariable<?>) type).getGenericDeclaration();
            if (genericDeclaration != BasicDao.class) {
                throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unsupported generic declaration " +
                        genericDeclaration);
            }
            Type[] bounds = ((TypeVariable<?>) type).getBounds();

            if (bounds[0] == Object.class) {
                String name = ((TypeVariable<?>) type).getName();
                if (StringUtils.equals(ENTITY, name)) {
                    return entityType;
                } else if (StringUtils.equals(ID, name)) {
                    return primaryKeyType;
                } else {
                    throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unknown type variable " + type);
                }
            } else {
                return resolveParameterType(bounds[0]);
            }
        }
        throw new EasyOrmException(ErrorCode.UNSUPPORTED_OPERATION_ERROR, "unknown type " + type);
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public IEntityMapper getEntityMapper() {
        return entityMapperFactory.create(entityType);
    }

    @Override
    public List<IParameterMapper> getParameterMappers() {
        return parameterMappers;
    }

    @Override
    public boolean isPrimaryKeyMode() {
        return isSpecifiedMode(MODE_PRIMARY_KEY);
    }

    @Override
    public boolean isComplexMode() {
        return isSpecifiedMode(MODE_COMPLEX);
    }

    @Override
    public boolean isEntityMode() {
        return isSpecifiedMode(MODE_ENTITY);
    }

    @Override
    public boolean isEntityCollectionMode() {
        return isSpecifiedMode(MODE_ENTITY_COLLECTION);
    }

    @Override
    public boolean isLockMode() {
        return isSpecifiedMode(MODE_LOCK);
    }

    @Override
    public boolean isInsertIgnoreMode() {
        return isSpecifiedMode(MODE_INSERT_IGNORE);
    }

    private void appendMode(long mode) {
        this.mode |= mode;
    }

    public int getWhereAt() {
        return whereAt;
    }

    public int getOffsetAt() {
        return offsetAt;
    }

    public int getLimitAt() {
        return limitAt;
    }

    private boolean isSpecifiedMode(long mode) {
        return (this.mode & mode) == mode;
    }
}
