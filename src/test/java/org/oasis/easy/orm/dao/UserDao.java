package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Dao
public interface UserDao {
    @Sql("select * from user where id=:id")
    Long findByPrimaryKey(@SqlParam("id") Long id);
}
