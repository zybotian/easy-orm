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

    @Test
    public void testInserts() throws Exception {
        User user1 = newUser().setName("乔峰").setAge(35).setAddress("大辽");
        User user2 = newUser().setName("虚竹").setAge(23).setAddress("灵鹫宫缥缈峰");
        User user3 = newUser().setName("段誉").setAge(18).setAddress("大理");
        User user4 = newUser().setName("鸠摩智").setAge(33).setAddress("吐蕃");

        boolean insert = advUserDao.insert(Arrays.asList(user1, user2, user3, user4));
        Assert.assertTrue(insert);

        User user5 = advUserDao.find(5L);
        User user6 = advUserDao.find(6L);
        User user7 = advUserDao.find(7L);
        User user8 = advUserDao.find(8L);

        Assert.assertEquals("乔峰", user5.getName());
        Assert.assertEquals("大辽", user5.getAddress());
        Assert.assertTrue(35 == user5.getAge());

        Assert.assertEquals("虚竹", user6.getName());
        Assert.assertEquals("灵鹫宫缥缈峰", user6.getAddress());
        Assert.assertTrue(23 == user6.getAge());

        Assert.assertEquals("段誉", user7.getName());
        Assert.assertEquals("大理", user7.getAddress());
        Assert.assertTrue(18 == user7.getAge());

        Assert.assertEquals("鸠摩智", user8.getName());
        Assert.assertEquals("吐蕃", user8.getAddress());
        Assert.assertTrue(33 == user8.getAge());

    }

    private User newUser() {
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
        return user;
    }

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
    public void testFindById() throws Exception {
        User user = advUserDao.findById(1L);
        Assert.assertTrue(1 == user.getId());
        Assert.assertTrue(18 == user.getAge());
        Assert.assertTrue(100 == user.getGroupId());
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("test-name", user.getName());
        Assert.assertEquals("hubei", user.getAddress());
    }

    @Test
    public void testFindByAgeRange() throws Exception {
        List<User> users = advUserDao.findByAgeRange(0, 30);
        Assert.assertTrue(users.size() == 4);
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
        Assert.assertTrue(18 == users.get(0).getAge());
        Assert.assertTrue(19 == users.get(1).getAge());
        Assert.assertTrue(20 == users.get(2).getAge());
        Assert.assertTrue(21 == users.get(3).getAge());
    }

    @Test
    public void testFindByName() throws Exception {
        List<User> users = advUserDao.findByName("test%");
        Assert.assertTrue(users.size() == 4);
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
        List<User> users2 = advUserDao.findByName("Test%");
        Assert.assertTrue(users2.size() == 0);
    }

    @Test
    public void testFindByIdList() throws Exception {
        List<User> users = advUserDao.findByIdList(Arrays.asList(1L, 2L, 3L, 4L));
        Assert.assertTrue(users.size() == 4);
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
    }

    @Test
    public void testFindByGroups() throws Exception {
        List<User> users1 = advUserDao.findByGroups(Arrays.asList(1, 2, 3));
        Assert.assertTrue(users1.size() == 0);

        List<User> users2 = advUserDao.findByGroups(Arrays.asList(100, 101, 301, 404));
        Collections.sort(users2, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        Assert.assertTrue(users2.size() == 4);
        Assert.assertTrue(1 == users2.get(0).getId());
        Assert.assertTrue(2 == users2.get(1).getId());
        Assert.assertTrue(3 == users2.get(2).getId());
        Assert.assertTrue(4 == users2.get(3).getId());
    }

    @Test
    public void testFindList() throws Exception {
        List<User> list = advUserDao.findList(0, 10);
        Assert.assertTrue(list.size() == 4);

        List<User> list1 = advUserDao.findList(0, 1);
        Assert.assertTrue(list1.size() == 1);

        List<User> list2 = advUserDao.findList(0, 4);
        Assert.assertTrue(list2.size() == 4);

        List<User> list3 = advUserDao.findList(2, 1);
        Assert.assertTrue(list3.size() == 1);
        Assert.assertTrue(list3.get(0).getId() == 3);
    }

    @Test
    public void testFindByAgeRangeList() throws Exception {
        // (18,21)
        List<User> users = advUserDao.findByAgeRange(18, 21, 0, 10);
        Assert.assertTrue(users.size() == 2);
        Assert.assertTrue(users.get(0).getAge() == 19);
        Assert.assertTrue(users.get(1).getAge() == 20);

        List<User> users2 = advUserDao.findByAgeRange(19, 25, 0, 10);
        Assert.assertTrue(users2.size() == 2);
        Assert.assertTrue(users2.get(0).getAge() == 20);
        Assert.assertTrue(users2.get(1).getAge() == 21);

        List<User> users3 = advUserDao.findByAgeRange(10, 25, 0, 10);
        Assert.assertTrue(users3.size() == 4);
        Assert.assertTrue(users3.get(0).getAge() == 18);
        Assert.assertTrue(users3.get(1).getAge() == 19);
        Assert.assertTrue(users3.get(2).getAge() == 20);
        Assert.assertTrue(users3.get(3).getAge() == 21);

        List<User> users4 = advUserDao.findByAgeRange(10, 25, 0, 2);
        Assert.assertTrue(users4.size() == 2);
        Assert.assertTrue(users4.get(0).getAge() == 18);
        Assert.assertTrue(users4.get(1).getAge() == 19);

        List<User> users5 = advUserDao.findByAgeRange(10, 25, 2, 2);
        Assert.assertTrue(users5.size() == 2);
        Assert.assertTrue(users5.get(0).getAge() == 20);
        Assert.assertTrue(users5.get(1).getAge() == 21);
    }
}
