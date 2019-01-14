package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.*;
import org.oasis.easy.orm.mapper.sql.Order;
import org.oasis.easy.orm.model.User;

import java.util.List;

/**
 * @author tianbo
 * @date 2019-01-07
 * @notes 若使用自动生成sql功能, 需要通过SqlParam指定参数名字, Offset/Limit除外
 */
@Dao
public interface AdvUserDao extends BasicDao<User, Long> {
    //---------------------------------------------------------
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

    //---------------------------------------------------------
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

    //---------------------------------------------------------

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