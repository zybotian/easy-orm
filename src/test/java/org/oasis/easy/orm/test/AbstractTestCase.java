package org.oasis.easy.orm.test;

import junit.framework.TestCase;

import org.oasis.easy.orm.dao.CreateTableDao;
import org.oasis.easy.orm.dao.UserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public abstract class AbstractTestCase extends TestCase {

    private static final String CONFIG_LOCATION = "applicationContext-test.xml";

    private static ClassPathXmlApplicationContext springContext;

    private static boolean userInfoTable;

    @Autowired
    private CreateTableDao createTableDao;

    @Autowired
    private UserDao userDao;

    protected static User createUser() {
        User user = new User();
        user.setName("test-name");
        user.setAge(18);
        user.setGroupId(100);
        user.setAddress("address");
        user.setMarried(true);
        user.setSalary(Double.valueOf("12000"));
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        return user;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (springContext == null) {
            // spring bean容器初始化
            springContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        }
        springContext.getAutowireCapableBeanFactory().autowireBean(this);

        createTableDao.createUserTable();
        userDao.insertOne(
                createUser().setAge(18).setAddress("hubei").setGroupId(100).setMarried(false).setSalary(100.0));
        userDao.insertOne(
                createUser().setAge(19).setAddress("hunan").setGroupId(101).setMarried(true).setSalary(200.0));
//        userDao.insertOne(
//                createUser().setAge(20).setAddress("shanghai").setGroupId(100).setMarried(false).setSalary(300.0));
//        userDao.insertOne(
//                createUser().setAge(21).setAddress("jiangsu").setGroupId(101).setMarried(true).setSalary(400.0));
        userDao.insertList(
                Arrays.asList(
                        createUser().setAge(20).setAddress("shanghai").setGroupId(100).setMarried(false).setSalary(300.0),
                        createUser().setAge(21).setAddress("jiangsu").setGroupId(101).setMarried(true).setSalary(400.0)
                ));
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        createTableDao.dropUserTable();
    }

    protected void sort(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
    }
}
