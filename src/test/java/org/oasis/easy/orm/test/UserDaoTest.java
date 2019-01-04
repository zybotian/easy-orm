package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.oasis.easy.orm.dao.UserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class UserDaoTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    public void testQuery() throws Exception {
        User user = userDao.findByPrimaryKey(1L);
        Assert.assertEquals(1, user.getId().longValue());
        Assert.assertEquals(18, user.getAge().intValue());
        Assert.assertEquals(100, user.getGroupId().intValue());
        Assert.assertEquals("hubei", user.getAddress());
        Assert.assertEquals(false, user.getMarried());
        Assert.assertEquals(100, user.getSalary().intValue());

        User user2 = userDao.findByPrimaryKey(2L);
        Assert.assertEquals(2, user2.getId().longValue());
        Assert.assertEquals(19, user2.getAge().intValue());
        Assert.assertEquals(101, user2.getGroupId().intValue());
        Assert.assertEquals("hunan", user2.getAddress());
        Assert.assertEquals(true, user2.getMarried());
        Assert.assertEquals(200, user2.getSalary().intValue());

        User user3 = userDao.findByPrimaryKey(3L);
        Assert.assertEquals(3, user3.getId().longValue());
        Assert.assertEquals(20, user3.getAge().intValue());
        Assert.assertEquals(100, user3.getGroupId().intValue());
        Assert.assertEquals("shanghai", user3.getAddress());
        Assert.assertEquals(false, user3.getMarried());
        Assert.assertEquals(300, user3.getSalary().intValue());

        User user4 = userDao.findByPrimaryKey(4L);
        Assert.assertEquals(4, user4.getId().longValue());
        Assert.assertEquals(21, user4.getAge().intValue());
        Assert.assertEquals(101, user4.getGroupId().intValue());
        Assert.assertEquals("jiangsu", user4.getAddress());
        Assert.assertEquals(true, user4.getMarried());
        Assert.assertEquals(400, user4.getSalary().intValue());

        Assert.assertEquals(4, userDao.count());

        List<User> users = userDao.findByPagination(0, 100);
        Assert.assertEquals(4, users.size());

        Assert.assertEquals(18, users.get(0).getAge().intValue());
        Assert.assertEquals(100, users.get(0).getGroupId().intValue());
        Assert.assertEquals("hubei", users.get(0).getAddress());
        Assert.assertEquals(100, users.get(0).getSalary().intValue());

        Assert.assertEquals(19, users.get(1).getAge().intValue());
        Assert.assertEquals(101, users.get(1).getGroupId().intValue());
        Assert.assertEquals("hunan", users.get(1).getAddress());
        Assert.assertEquals(200, users.get(1).getSalary().intValue());

        Assert.assertEquals(20, users.get(2).getAge().intValue());
        Assert.assertEquals(100, users.get(2).getGroupId().intValue());
        Assert.assertEquals("shanghai", users.get(2).getAddress());
        Assert.assertEquals(300, users.get(2).getSalary().intValue());

        Assert.assertEquals(21, users.get(3).getAge().intValue());
        Assert.assertEquals(101, users.get(3).getGroupId().intValue());
        Assert.assertEquals("jiangsu", users.get(3).getAddress());
        Assert.assertEquals(400, users.get(3).getSalary().intValue());
    }

    public void testDelete() throws Exception {
        Assert.assertEquals(4, userDao.count());

        int deleted = userDao.deleteOne(1L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(3, userDao.count());

        deleted = userDao.deleteOne(2L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(2, userDao.count());

        deleted = userDao.deleteOne(3L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(1, userDao.count());

        deleted = userDao.deleteOne(4L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(0, userDao.count());

        deleted = userDao.deleteOne(1L);
        Assert.assertTrue(deleted == 0);
        Assert.assertEquals(0, userDao.count());
    }

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
