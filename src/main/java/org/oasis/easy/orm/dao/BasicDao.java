package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.*;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public interface BasicDao<ENTITY, ID> {

    /**
     * 按照id查询
     */
    ENTITY find(ID id);

    /**
     * 插入一条记录
     */
    @ReturnGeneratedKeys
    ID insert(ENTITY entity);

    /**
     * 批量插入
     */
    @Batchable
    boolean insert(List<ENTITY> entities);

    /**
     * 更新一条记录
     */
    boolean update(ENTITY entity);

    /**
     * 批量修改记录
     */
    @Batchable
    boolean update(List<ENTITY> entities);

    /**
     * 按照id删除
     */
    boolean delete(ID id);
}
