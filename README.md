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
     */
    ENTITY find(ID id);

    /**
     * 悲观锁
     */
    @Lock
    ENTITY selectForUpdate(ID id);

    /**
     * 插入一条记录
     */
    @ReturnGeneratedKeys
    ID insert(ENTITY entity);

    /**
     * 插入一条记录,如果主键冲突则忽略
     */
    @InsertIgnore
    @ReturnGeneratedKeys
    ID insertIgnore(ENTITY entity);

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
```

The ***BasicDao*** provided basic CRUD supports, we can extends ***BasicDao*** to extend our dao class, like this:
```java
@Dao
public interface UserDao extends BasicDao<User, Long> {
    //--------------------------select-------------------------------
    User findById(@SqlParam("id") Long id);
    
    List<User> findList(@Offset Integer limit,
                        @Limit Integer offset);
    
    List<User> findByIdList(@SqlParam("id") @In List<Long> idList);
    
    List<User> findByName(@SqlParam("name") @Like String name);
    
    List<User> findByGroups(@SqlParam("groupId") @In List<Integer> groups);
    
    List<User> findByAgeRange(@SqlParam("age") @Ge Integer minAge,
                              @SqlParam("age") @Le Integer maxAge);
    
    List<User> findByAgeRange(@SqlParam("age") @Gt Integer minAge,
                              @SqlParam("age") @Lt Integer maxAge,
                              @Offset Integer limit,
                              @Limit Integer offset);
    
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
    
    List<User> findByMarried(@SqlParam("married") Boolean married);
    
    /**
     * count(distinct(id))
     */
    @Count
    int countDistinctId();
    
    @Count(value = "groupId")
    int countDistinctGroupId();
    
    @Count(value = "groupId", distinct = false)
    int countGroupId();
    
    
    //----------------------------update-----------------------------
    int updateAddress(@SqlParam("address") String address,
                      @Where
                      @SqlParam("id") Long id);
    
    int updateName(@SqlParam("name") String name,
                   @Where
                   @SqlParam("id") Long id);
    
    int updateGroupId(@SqlParam("groupId") Integer groupId,
                      @Where
                      @SqlParam("id") Long id);
    
    int updateAge(@SqlParam("age") Integer age,
                  @Where
                  @SqlParam("id") Long id);
    
    int updateNameAddress(@SqlParam("name") String name,
                          @SqlParam("address") String address,
                          @Where
                          @SqlParam("id") Long id);
    
    int updateByGroupId(@SqlParam("address") String address,
                        @Where
                        @SqlParam("groupId") Integer groupId);
    
    int updateNameAddressByGroupList(@SqlParam("name") String name,
                                     @SqlParam("address") String address,
                                     @Where
                                     @SqlParam("groupId") @In List<Integer> groupIds);
    
    int updateNameByAgeRangeAddress(@SqlParam("name") String name,
                                    @SqlParam("updateTime") Long updateTime,
                                    @Where
                                    @SqlParam("age") @Ge Integer minAge,
                                    @SqlParam("age") @Le Integer maxAge,
                                    @SqlParam("address") @Like String address);
    
    
    //---------------------------delete------------------------------
    int deleteById(@SqlParam("id") Long id);
    
    int deleteByGroupId(@SqlParam("groupId") Integer groupId);
    
    int deleteByName(@SqlParam("name") String name);
    
    int deleteByMarried(@SqlParam("married") Boolean married);
    
    int deleteByNameLike(@SqlParam("name") @Like String name);
    
    int deleteByIdList(@SqlParam("id") @In List<Long> idList);
    
    int deleteByGroupIdList(@SqlParam("groupId") @In List<Integer> groupIdList);
    
    int delete(@SqlParam("name") @Like String name,
               @SqlParam("groupId") @In List<Integer> groupIds,
               @SqlParam("age") @Ge Integer minAge,
               @SqlParam("age") @Lt Integer maxAge);
}
```





