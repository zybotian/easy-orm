# easy-orm

## 1. how to use
### 1.1 pom.xml configuration
```xml
<project>
    <repositories>
        <repository>
            <id>mvn-repo-easy-orm</id>
            <!--url: https://raw.github.com/仓库拥有者名字(非登录名)/项目名字/分支-->
            <url>https://raw.github.com/zybotian/mvn-repo/easy-orm</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
    
    <dependencies>
        <!--引用github上的mvn依赖-->
        <dependency>
            <groupId>org.oasis</groupId>
            <artifactId>easy-orm</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```
### 1.2 applicationContext.xml configuration:
```xml
<beans>
 <!--  easy orm configuration -->
    <bean class="org.oasis.easy.orm.core.EasyOrmBeanFactoryPostProcessor">
        <constructor-arg value="org.oasis.easy.orm.dao"/>
    </bean>
    
    <!--不配置的话,不具备自动生成sql的能力-->
    <bean class="org.oasis.easy.orm.interpreter.SqlManagerInterpreter">
    </bean>
</beans>
```
## 2. benefits
### 2.1 as convenient as mybatis
```java
public interface UserDao {
      @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " where id=:1")
      User findByPrimaryKey(Long id);
      
      @Sql("insert into " + TABLE_NAME + "(" + INSERT_COLUMNS + ")" + "values(" + INSERT_VALUES + ")")
      int insertOne(User user);
      
      @Sql("update " + TABLE_NAME + " set " + UPDATE_COLUMNS + " where id=:1.id")
      int updateOne(User user);
      
      @Sql("delete from " + TABLE_NAME + " where id=:1")
      int deleteOne(Long id);
}
```

### 2.2 simpler than mybatis
The BasicDao class definition:
```java
public interface BasicDao<ENTITY, ID> {

    /**
     * 按照id查询
     * easy orm will auto generate sql like:
     * select col_1,col_2...col_n from table_name where id=#{id}
     */
    ENTITY find(ID id);
    
    /**
     * 悲观锁
     * easy orm will auto generate sql like:
     * select col_1,col_2...col_n from table_name where id=#{id} for update
     */
    @Lock
    ENTITY selectForUpdate(ID id);
    
    /**
     * 插入一条记录
     * easy orm will auto generate sql like:
     * insert into table_name(col_1,col_2,col_3...col_n) values (:1.field_1,:1.field_2,:1.field_3...:1.field_n); 
     */
    @ReturnGeneratedKeys
    ID insert(ENTITY entity);
    
    /**
     * 插入一条记录,如果主键冲突则忽略
     * easy orm will auto generate sql like:
     * insert ignore into table_name(col_1,col_2,col_3...col_n) values (:1.field_1,:1.field_2,:1.field_3...:1.field_n); 
     */
    @InsertIgnore
    @ReturnGeneratedKeys
    ID insertIgnore(ENTITY entity);
    
    /**
     * 批量插入
     * easy orm will auto generate sql like:
     * insert into table_name(col_1,col_2,col_3...col_n) values (:1.field_1,:1.field_2,:1.field_3...:1.field_n); 
     * ......
     * insert into table_name(col_1,col_2,col_3...col_n) values (:1.field_1,:1.field_2,:1.field_3...:1.field_n); 
     */
    @Batchable
    boolean insert(List<ENTITY> entities);
    
    /**
     * 更新一条记录
     * easy orm will auto generate sql like:
     * update table_name set col_1=:1.field_1, col_2=:1.field_2....col_n=:1.field_n where id=:1.id
     */
    boolean update(ENTITY entity);
    
    /**
     * 批量修改记录
     * update table_name set col_1=:1.field_1, col_2=:1.field_2....col_n=:1.field_n where id=:1.id
     * ...
     * update table_name set col_1=:1.field_1, col_2=:1.field_2....col_n=:1.field_n where id=:1.id
     */
    @Batchable
    boolean update(List<ENTITY> entities);
    
    /**
     * 按照id删除
     * delete from table_name where id=:1
     */
    boolean delete(ID id);
}
```

The ***BasicDao*** provided basic CRUD supports, we can extends ***BasicDao*** to extend our dao class, like this:
```java
@Dao
public interface UserDao extends BasicDao<User, Long> {
    //--------------------------select-------------------------------
    /**
     * select col_1,col_2...col_n from user where id=:1;  
     */
    User findById(@SqlParam("id") Long id);
    
    /**
     * select col_1, col_2...col_n from user limit :1,:2; 
     */
    List<User> findList(@Offset Integer limit,
                        @Limit Integer offset);
    
    /**
     * select col_1,col_2,...col_n from user where id in (:1); 
     */
    List<User> findByIdList(@SqlParam("id") @In List<Long> idList);
    
    /**
     * select col_1,col_2,...col_n from user where name like :1; 
     */
    List<User> findByName(@SqlParam("name") @Like String name);
    
    /**
     * select col_1,col_2,...col_n from user where group_id in (:1); 
     */
    List<User> findByGroups(@SqlParam("groupId") @In List<Integer> groups);
    
    /**
     * select col_1,col_2,...col_n from user where age >= :1 and age <= :2; 
     */
    List<User> findByAgeRange(@SqlParam("age") @Ge Integer minAge,
                              @SqlParam("age") @Le Integer maxAge);
    
    /**
     * select col_1,col_2,...col_n from user where age >= :1 and age <= :2 limit :3,:4; 
     */
    List<User> findByAgeRange(@SqlParam("age") @Gt Integer minAge,
                              @SqlParam("age") @Lt Integer maxAge,
                              @Offset Integer limit,
                              @Limit Integer offset);
    /**
      * select col_1,col_2,...col_n from user where age >= :1 and age <= :2
      * and name like :3
      * and address like :4
      * and group _id in (:5)
      * and salary >= :6
      * and salary <= :7
      * order by field asc
      * limit :8,:9; 
     */
    List<User> findList(@SqlParam("age") @Gt Integer minAge,
                        @SqlParam("age") @Lt Integer maxAge,
                        @SqlParam("name") @Like String name,
                        @SqlParam("address") @Like String address,
                        @SqlParam("groupId") @In List<Integer> groupIds,
                        @SqlParam("salary") @Ge Double minSalary,
                        @SqlParam("salary") @Le Double maxSalary,
                        @OrderBy Order order,
                        @Offset Integer limit,
                        @Limit Integer offset);
    /**
     * select col_1,col_2,...col_n from user where married = :1; 
     */
    List<User> findByMarried(@SqlParam("married") Boolean married);
    
    /**
     * select count(distinct(id)) from user;
     */
    @Count
    int countDistinctId();
    
    /**
     * select count(distinct(group_id)) from user; 
     */
    @Count(value = "groupId")
    int countDistinctGroupId();
    
    /**
     * select count(group_id) from user; 
     */
    @Count(value = "groupId", distinct = false)
    int countGroupId();
    
    
    //----------------------------update-----------------------------
    /**
     * update user set address=:1  where id=:2
     */
    int updateAddress(@SqlParam("address") String address,
                      @Where
                      @SqlParam("id") Long id);
    
     /**
      * update user set name=:1  where id=:2
      */
    int updateName(@SqlParam("name") String name,
                   @Where
                   @SqlParam("id") Long id);
     /**
      * update user set address=:1  where id=:2
      */
    int updateGroupId(@SqlParam("groupId") Integer groupId,
                      @Where
                      @SqlParam("id") Long id);
    
     /**
      * update user set age=:1  where id=:2
      */
    int updateAge(@SqlParam("age") Integer age,
                  @Where
                  @SqlParam("id") Long id);
    
    /**
     * update user set name=:1, address=:2  where id=:3
     */
    int updateNameAddress(@SqlParam("name") String name,
                          @SqlParam("address") String address,
                          @Where
                          @SqlParam("id") Long id);
    
    /**
     * update user set address=:1 where group_id=:2
     */
    int updateByGroupId(@SqlParam("address") String address,
                        @Where
                        @SqlParam("groupId") Integer groupId);
    
    /**
     * update user set name=:1,address=:2 where group_id in (:3);
     */
    int updateNameAddressByGroupList(@SqlParam("name") String name,
                                     @SqlParam("address") String address,
                                     @Where
                                     @SqlParam("groupId") @In List<Integer> groupIds);
    
    /**
      * update user set name=:1, update_time=:2 where age >= :3 and age <= :4 and address like :5;
      */
    int updateNameByAgeRangeAddress(@SqlParam("name") String name,
                                    @SqlParam("updateTime") Long updateTime,
                                    @Where
                                    @SqlParam("age") @Ge Integer minAge,
                                    @SqlParam("age") @Le Integer maxAge,
                                    @SqlParam("address") @Like String address);
    
    
    //---------------------------delete------------------------------
    /**
     * delete from user  where id=:1
     */
    int deleteById(@SqlParam("id") Long id);
    
    /**
     * delete from user where group_id=:1
     */
    int deleteByGroupId(@SqlParam("groupId") Integer groupId);
    
    /**
     * delete from user where name like :1
     */
    int deleteByName(@SqlParam("name") String name);
    
    /**
     * delete from user  where married = :1
     */
    int deleteByMarried(@SqlParam("married") Boolean married);
    
    /**
     * delete from user  where name like :1
     */
    int deleteByNameLike(@SqlParam("name") @Like String name);
    
    /**
     * delete from user  where id in (:1)
     */
    int deleteByIdList(@SqlParam("id") @In List<Long> idList);
    
    /**
     * delete from user  where group_id in (:1)
     */
    int deleteByGroupIdList(@SqlParam("groupId") @In List<Integer> groupIdList);
    
    /**
     * delete from user  where name like :1 and group_id in (:2) and age >= :3 and age < :4
     */
    int delete(@SqlParam("name") @Like String name,
               @SqlParam("groupId") @In List<Integer> groupIds,
               @SqlParam("age") @Ge Integer minAge,
               @SqlParam("age") @Lt Integer maxAge);
}
```