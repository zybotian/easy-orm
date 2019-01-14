package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
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

    @Test
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

    @Test
    public void testUpsert() throws Exception {

        User one = new User()
                .setSalary(123.0)
                .setMarried(true)
                .setAddress("上海")
                .setGroupId(120)
                .setAge(134)
                .setName("mala")
                .setCreateTime(123456L)
                .setUpdateTime(123456L);
        int updated = userDao.upsertOne(one);
        Assert.assertEquals(1, updated);

        User user = userDao.findByPrimaryKey(5L);
        Assert.assertEquals(5, user.getId().longValue());
        Assert.assertEquals(134, user.getAge().intValue());
        Assert.assertEquals(120, user.getGroupId().intValue());
        Assert.assertEquals("上海", user.getAddress());
        Assert.assertEquals(true, user.getMarried());
        Assert.assertEquals(123, user.getSalary().intValue());

        user.setUpdateTime(123456789L);
        user.setAge(120);
        user.setGroupId(9999);
        int upsert = userDao.upsertOne(user);
        Assert.assertTrue(upsert > 0);
        User user1 = userDao.findByPrimaryKey(5L);
        Assert.assertTrue(user1.getAge() == 120);
        Assert.assertTrue(user1.getGroupId() == 9999);
        Assert.assertTrue(123456789L == user1.getUpdateTime());
    }

    @Test
    public void testUpdateAge() throws Exception {
        User user = userDao.findByPrimaryKey(1L);
        Assert.assertTrue(18 == user.getAge());
        int updated = userDao.updateAge(39, 39393939L, 1L);
        Assert.assertTrue(updated > 0);
        User user1 = userDao.findByPrimaryKey(1L);
        Assert.assertTrue(user1.getAge() == 39);
    }
}
