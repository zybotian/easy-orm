package org.oasis.easy.orm.mapper.sql.impl;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.dao.BasicDao;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.IEntityMapper;
import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementMetadata;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class ConditionOperationMapper implements IOperationMapper {

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

    public ConditionOperationMapper(StatementMetadata statementMetadata) {
        this.statementMetadata = statementMetadata;
        this.entityType = statementMetadata.getDaoMetadata().resolveTypeVariable(BasicDao.class, "ENTITY");
        this.primaryKeyType = statementMetadata.getDaoMetadata().resolveTypeVariable(BasicDao.class, "ID");
        this.operationName = generateOperationName();
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

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public IEntityMapper getEntityMapper() {
        return entityMapperFactory.create(entityType);
    }

    @Override
    public void setEntityMapperFactory(EntityMapperFactory entityMapperFactory) {
        this.entityMapperFactory = entityMapperFactory;
    }
}
