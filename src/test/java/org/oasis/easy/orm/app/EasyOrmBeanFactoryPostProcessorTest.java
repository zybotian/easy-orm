package org.oasis.easy.orm.app;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmBeanFactoryPostProcessorTest extends TestCase {

    ClassPathXmlApplicationContext springContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        String location = "applicationContext-test.xml";
        springContext = new ClassPathXmlApplicationContext(location);
    }

    @Test
    public void testBeanFactory() throws Exception {
        String[] beanDefinitionNames = springContext.getBeanDefinitionNames();
        List<String> beanDefinationNames = Arrays.asList(beanDefinitionNames);
        Assert.assertTrue(beanDefinationNames.contains("org.oasis.easy.orm.dao.BookDao"));
        Assert.assertTrue(beanDefinationNames.contains("org.oasis.easy.orm.dao.UserDao"));
        Assert.assertFalse(beanDefinationNames.contains("org.oasis.easy.orm.dao.WrongDao"));
        Assert.assertFalse(beanDefinationNames.contains("org.oasis.easy.orm.dao.IllegalDao"));
    }
}
