package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.*;
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

}