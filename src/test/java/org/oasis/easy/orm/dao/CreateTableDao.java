package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.Dao;
import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.constant.SqlType;

/**
 * @author Paul
 * @date 2018-12-30
 */
@Dao
public interface CreateTableDao {
    String createUserTable = "create table user "
            + "(id bigint not null auto_increment primary key"
            + ",name varchar(32) not null "
            + ",age int not null"
            + ",address varchar(16) not null"
            + ",married int not null"
            + ",salary decimal(20,2) not null"
            + ",create_time bigint not null"
            + ",update_time bigint not null"
            + ")";

    String dropUserTable = "drop table user if exists";

    @Sql(value = createUserTable, type = SqlType.WRITE)
    void createUserTable();

    @Sql(value = dropUserTable, type = SqlType.WRITE)
    void dropUserTable();
}
