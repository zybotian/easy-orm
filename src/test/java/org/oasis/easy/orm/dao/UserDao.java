package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.*;
import org.oasis.easy.orm.model.*;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Dao
public interface UserDao extends BasicDao<User, Long> {
    String TABLE_NAME = "`user`";
    String INSERT_COLUMNS = "`name`,`age`,`group_id`,`address`,`married`,`salary`,`create_time`,`update_time`";
    String SELECT_COLUMNS = "`id`," + INSERT_COLUMNS;
    String INSERT_VALUES = ":1.name,:1.age,:1.groupId,:1.address,:1.married,:1.salary,:1.createTime,:1.updateTime";
    String UPDATE_COLUMNS = "name=:1.name,age=:1.age,group_id=:1.groupId,address=:1.address,married=:1.married,salary=:1.salary,update_time=:1.updateTime";

    //----------------------------------------查询类-------------------------------------------------------
    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " where id=:1")
    User findByPrimaryKey(Long id);

    @Sql("select count(`id`) from " + TABLE_NAME)
    int count();

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    List<User> findList(int offset, int limit);

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    Collection<User> findCollection(int offset, int limit);

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    Set<User> findSet(int offset, int limit);

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    User[] findArray(int offset, int limit);

    @Sql("select `##(:1)` from " + TABLE_NAME + " limit :2,:3")
    List<String> findListSingle(String field, int offset, int limit);

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " limit :1,:2")
    List<Map<String, Object>> findMap(int offset, int limit);

    @Sql("select `##(:1)` from " + TABLE_NAME + " limit :2,:3")
    Set<String> findSetSingle(String field, int offset, int limit);

    @Sql("select `##(:1)` from " + TABLE_NAME + " limit :2,:3")
    Collection<Integer> findCollectionSingle(String field, int offset, int limit);

    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME
            + " where 1=1"
            + "#if(:1.name!=null) { and `name` like :1.name}"
            + "#if(:1.minAge!=null) { and `age`>=:1.minAge}"
            + "#if(:1.maxAge!=null) { and `age`<=:1.maxAge}"
            + "#if(:1.groups!=null) { and `group_id` in (:1.groups)}"
            + "#if(:1.married!=null) { and `married`=:1.married}"
            + "#if(:1.address!=null) { and locate(:1.address,`address`)>0}"
            + "#if(:1.orderBy!=null && :1.orderType!=null) { order by ##(:1.orderBy) ##(:1.orderType)}"
            + "#if(:1.offset!=null && :1.pageSize!=null) "
            + "{ limit :1.offset,:1.pageSize}")
    List<User> findListAdv(UserQuery query);

    @Sql("select `##(:1)` from " + TABLE_NAME + " limit :2,:3")
    Boolean[] findArraySingle(String field, int offset, int limit);

    @Sql("select count(1) as count, group_id as groupId from " + TABLE_NAME + " group by group_id order by group_id")
    List<GroupResult> countGroup();

    //----------------------------------------插入类-------------------------------------------------------
    @Sql("insert into " + TABLE_NAME + "(" + INSERT_COLUMNS + ")" + "values(" + INSERT_VALUES + ")")
    int insertOne(User user);

    @Batchable
    @Sql("insert into " + TABLE_NAME + "(" + INSERT_COLUMNS + ")" + "values(" + INSERT_VALUES + ")")
    int insertList(List<User> user);

    @Sql("INSERT INTO " + TABLE_NAME + "(" + SELECT_COLUMNS + ") VALUES(" + ":1.id," + INSERT_VALUES + ")"
            + " ON DUPLICATE KEY UPDATE " + UPDATE_COLUMNS
    )
    int upsertOne(User user);

    //----------------------------------------修改类-------------------------------------------------------
    @Sql("update " + TABLE_NAME + " set " + UPDATE_COLUMNS + " where id=:1.id")
    int updateOne(User user);

    @Sql("update " + TABLE_NAME + " set age=:1, update_time=:2 where id=:3")
    int updateAge(Integer age, Long updateTime, Long id);


    //----------------------------------------删除类-------------------------------------------------------
    @Sql("delete from " + TABLE_NAME + " where id=:1")
    int deleteOne(Long id);
}
