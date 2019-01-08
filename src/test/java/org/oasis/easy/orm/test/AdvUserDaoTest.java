package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.AdvUserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class AdvUserDaoTest extends AbstractTestCase {

    @Autowired
    private AdvUserDao advUserDao;

    @Test
    public void testFind() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(1 == user.getId());
        Assert.assertTrue(18 == user.getAge());
        Assert.assertTrue(100 == user.getGroupId());
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("test-name", user.getName());
        Assert.assertEquals("hubei", user.getAddress());
    }

    @Test
    public void testDelete() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(user != null);
        boolean delete = advUserDao.delete(1L);
        Assert.assertTrue(delete);
        User user1 = advUserDao.find(1L);
        Assert.assertTrue(user1 == null);
    }

    @Test
    public void testInsert() throws Exception {
        User user = new User();
        user.setAge(35);
        user.setGroupId(1);
        // 假装福建人
        user.setAddress("hujian");
        user.setName("qiaofeng");
        user.setMarried(false);
        user.setSalary(0.0);
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        long id = advUserDao.insert(user);
        Assert.assertTrue(id > 0);
        User user1 = advUserDao.find(id);
        Assert.assertTrue(user1.getId() == id);
        Assert.assertTrue(user1.getAge() == 35);
        Assert.assertTrue(user1.getGroupId() == 1);
        Assert.assertTrue(user1.getSalary().intValue() == 0);
        Assert.assertEquals("hujian", user1.getAddress());
        Assert.assertEquals("qiaofeng", user1.getName());
        Assert.assertFalse(user1.getMarried());

        user.setAge(178);
        user.setName("扫地僧");
        user.setAddress("少林寺藏经阁");
        Long id2 = advUserDao.insert(user);
        User user2 = advUserDao.find(id2);
        Assert.assertTrue(id2 > 0);
        Assert.assertEquals("扫地僧", user2.getName());
        Assert.assertEquals("少林寺藏经阁", user2.getAddress());
        Assert.assertTrue(user2.getAge() == 178);
    }
}
