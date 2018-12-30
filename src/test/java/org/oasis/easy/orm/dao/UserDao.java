package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.Dao;
import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.annotations.SqlParam;
import org.oasis.easy.orm.model.User;

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

    @Sql("select * from user where id=:id")
    User findByPrimaryKey(@SqlParam("id") Long id);

    @Sql("insert into " + TABLE_NAME + " ( " + INSERT_COLUMNS + " ) " + "values ( " + INSERT_VALUES + " )")
    int insertOne(@SqlParam("user") User user);
}
