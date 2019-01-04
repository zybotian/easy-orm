package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.oasis.easy.orm.dao.UserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class UserDaoUpdateTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    public void testUpdate() throws Exception {
        User user = userDao.findByPrimaryKey(1L);
        Assert.assertEquals(1, user.getId().longValue());
        Assert.assertEquals(18, user.getAge().intValue());
        Assert.assertEquals(100, user.getGroupId().intValue());
        Assert.assertEquals("hubei", user.getAddress());
        Assert.assertEquals(false, user.getMarried());
        Assert.assertEquals(100, user.getSalary().intValue());

        long timestamp = System.currentTimeMillis();
        user.setMarried(true);
        user.setAge(180);
        user.setAddress("湖北");
        user.setUpdateTime(timestamp);
        int updated = userDao.updateOne(user);
        Assert.assertTrue(updated > 0);

        user = userDao.findByPrimaryKey(1L);
        Assert.assertEquals(180, user.getAge().intValue());
        Assert.assertEquals("湖北", user.getAddress());
        Assert.assertEquals(true, user.getMarried());
        Assert.assertEquals(timestamp, user.getUpdateTime().longValue());
    }
}
