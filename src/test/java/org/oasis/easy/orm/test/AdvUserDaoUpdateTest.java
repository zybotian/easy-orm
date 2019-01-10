package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.AdvUserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class AdvUserDaoUpdateTest extends AbstractTestCase {

    @Autowired
    private AdvUserDao advUserDao;

    @Test
    public void testUpdate() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(user.getAge() == 18);
        Assert.assertTrue(user.getGroupId() == 100);
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("hubei", user.getAddress());

        user.setAge(81);
        user.setGroupId(250);
        user.setAddress("IBM");
        user.setSalary(12000.0);
        boolean update = advUserDao.update(user);
        Assert.assertTrue(update);

        User user1 = advUserDao.find(1L);
        Assert.assertTrue(user1.getAge() == 81);
        Assert.assertTrue(user1.getGroupId() == 250);
        Assert.assertTrue(12000 == user.getSalary().intValue());
        Assert.assertEquals("IBM", user1.getAddress());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        User user1 = advUserDao.find(1L);
        Assert.assertEquals("hubei", user1.getAddress());
        int updated = advUserDao.updateAddress("上海市", 1L);
        Assert.assertTrue(updated > 0);
        User user2 = advUserDao.find(1L);
        Assert.assertEquals("上海市", user2.getAddress());
    }

    @Test
    public void testUpdateName() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertEquals("test-name", user.getName());
        int updated = advUserDao.updateName("郭靖大俠", 1L);
        Assert.assertTrue(updated > 0);
        User user1 = advUserDao.find(1L);
        Assert.assertEquals("郭靖大俠", user1.getName());
    }

    @Test
    public void testUpdateGroupId() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(100 == user.getGroupId());
        int updated = advUserDao.updateGroupId(235, 1L);
        Assert.assertTrue(updated > 0);
        User user1 = advUserDao.find(1L);
        Assert.assertTrue(235 == user1.getGroupId());
    }

    @Test
    public void testUpdateNameAddress() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertEquals("test-name", user.getName());
        Assert.assertEquals("hubei", user.getAddress());
        int updated = advUserDao.updateNameAddress("郭靖大俠", "嘉兴醉仙楼", 1L);
        Assert.assertTrue(updated > 0);
        User user1 = advUserDao.find(1L);
        Assert.assertEquals("郭靖大俠", user1.getName());
        Assert.assertEquals("嘉兴醉仙楼", user1.getAddress());
    }

    @Test
    public void testUpdateByGroupId() throws Exception {
        List<User> users = advUserDao.findByGroups(Arrays.asList(100));
        Assert.assertTrue(users.size() == 2);
        sort(users);
        Assert.assertEquals("hubei", users.get(0).getAddress());
        Assert.assertEquals("shanghai", users.get(1).getAddress());

        int updated = advUserDao.updateByGroupId("桃花岛", 100);
        Assert.assertTrue(updated > 0);

        List<User> users1 = advUserDao.findByGroups(Arrays.asList(100));
        Assert.assertTrue(users1.size() == 2);
        sort(users1);
        Assert.assertEquals("桃花岛", users1.get(0).getAddress());
        Assert.assertEquals("桃花岛", users1.get(1).getAddress());
    }

    @Test
    public void testUpdateNameAddressByGroupList() throws Exception {
        List<User> users = advUserDao.findByGroups(Arrays.asList(100, 101));
        Assert.assertTrue(users.size() == 4);
        Assert.assertEquals("test-name", users.get(0).getName());
        Assert.assertEquals("hubei", users.get(0).getAddress());
        Assert.assertEquals("test-name", users.get(1).getName());
        Assert.assertEquals("hunan", users.get(1).getAddress());
        Assert.assertEquals("test-name", users.get(2).getName());
        Assert.assertEquals("shanghai", users.get(2).getAddress());
        Assert.assertEquals("test-name", users.get(3).getName());
        Assert.assertEquals("jiangsu", users.get(3).getAddress());

        int updated = advUserDao.updateNameAddressByGroupList("黄老邪", "桃花岛", Arrays.asList(100));
        Assert.assertTrue(updated > 0);
        List<User> users1 = advUserDao.findByGroups(Arrays.asList(100, 101));
        Assert.assertTrue(users1.size() == 4);
        Assert.assertEquals("黄老邪", users1.get(0).getName());
        Assert.assertEquals("桃花岛", users1.get(0).getAddress());
        Assert.assertEquals("test-name", users1.get(1).getName());
        Assert.assertEquals("hunan", users1.get(1).getAddress());
        Assert.assertEquals("黄老邪", users1.get(2).getName());
        Assert.assertEquals("桃花岛", users1.get(2).getAddress());
        Assert.assertEquals("test-name", users1.get(3).getName());
        Assert.assertEquals("jiangsu", users1.get(3).getAddress());

        int updated2 = advUserDao.updateNameAddressByGroupList("黄老邪", "桃花岛", Arrays.asList(101));
        Assert.assertTrue(updated2 > 0);
        List<User> users2 = advUserDao.findByGroups(Arrays.asList(100, 101));
        Assert.assertTrue(users2.size() == 4);
        for (User user : users2) {
            Assert.assertEquals("黄老邪", user.getName());
            Assert.assertEquals("桃花岛", user.getAddress());
        }
    }

    private void sort(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
    }
}
