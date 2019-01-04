package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.Dao;
import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.model.User;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Dao
public interface UserDao {
    String TABLE_NAME = "`user`";
    String INSERT_COLUMNS = "`name`,`age`,`group_id`,`address`,`married`,`salary`,`create_time`,`update_time`";
    String SELECT_COLUMNS = "`id`," + INSERT_COLUMNS;
    String INSERT_VALUES = ":1.name,:1.age,:1.groupId,:1.address,:1.married,:1.salary,:1.createTime,:1.updateTime";
    String UPDATE_COLUMNS = "name=:1.name,age=:1.age,group_id=:1.groupId,address=:1.address,married=:1.married,salary=:1.salary,update_time=:1.updateTime";

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " where id=:1")
    User findByPrimaryKey(Long id);

    @Sql("select count(`id`) from " + TABLE_NAME)
    int count();

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    List<User> findByPagination(int offset, int limit);

    @Sql("insert into " + TABLE_NAME + " ( " + INSERT_COLUMNS + " ) " + "values ( " + INSERT_VALUES + " )")
    int insertOne(User user);

    @Sql("update " + TABLE_NAME + " set " + UPDATE_COLUMNS + " where id=:1.id")
    int updateOne(User user);

    @Sql("delete from " + TABLE_NAME + " where id=:1")
    int deleteOne(Long id);
}
