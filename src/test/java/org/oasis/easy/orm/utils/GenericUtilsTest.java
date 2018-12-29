package org.oasis.easy.orm.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class GenericUtilsTest {

    @Test
    public void testResolveTypeVariable() throws Exception {
        Method justTest = Generic.class.getDeclaredMethod("justTest", Object.class);
        Class returnType = GenericUtils.resolveTypeVariable(Generic.class, justTest.getGenericReturnType());
        Assert.assertTrue(List.class == returnType);

        Method findOne = Generic.class.getDeclaredMethod("findOne", String.class);
        Class returnType2 = GenericUtils.resolveTypeVariable(Generic.class, findOne.getGenericReturnType());
        Assert.assertEquals(GenericUtilsTest.People.class, returnType2);

        Method listAll = Generic.class.getDeclaredMethod("listAll", String.class);
        Class returnType3 = GenericUtils.resolveTypeVariable(Generic.class, listAll.getGenericReturnType());
        Assert.assertEquals(List.class, returnType3);

        Method listArr = Generic.class.getDeclaredMethod("listArr", String.class);
        Class returnType4 = GenericUtils.resolveTypeVariable(Generic.class, listArr.getGenericReturnType());
        Assert.assertEquals(GenericUtilsTest.People[].class, returnType4);

        Method listAll2 = Generic.class.getDeclaredMethod("listAll2");
        Class returnType5 = GenericUtils.resolveTypeVariable(Generic.class, listAll2.getGenericReturnType());
        Assert.assertEquals(Class.class, returnType5);
    }

    @Test
    public void testResolveTypeParameters() throws Exception {
        Method justTest = Generic.class.getDeclaredMethod("justTest", Object.class);
        Class[] parameterTypes1 = GenericUtils.resolveTypeParameters(Generic.class, justTest.getGenericReturnType());
        Assert.assertTrue(parameterTypes1.length == 1);
        Assert.assertEquals(Date.class, parameterTypes1[0]);

        Method findOne = Generic.class.getDeclaredMethod("findOne", String.class);
        Class[] parameterTypes2 = GenericUtils.resolveTypeParameters(Generic.class, findOne.getGenericReturnType());
        Assert.assertEquals(0, parameterTypes2.length);

        Method listAll = Generic.class.getDeclaredMethod("listAll", String.class);
        Class[] parameterTypes3 = GenericUtils.resolveTypeParameters(Generic.class, listAll.getGenericReturnType());
        Assert.assertEquals(1, parameterTypes3.length);
        Assert.assertEquals(People.class, parameterTypes3[0]);

        Method listArr = Generic.class.getDeclaredMethod("listArr", String.class);
        Class[] parameterTypes4 = GenericUtils.resolveTypeParameters(Generic.class, listArr.getGenericReturnType());
        Assert.assertEquals(0, parameterTypes4.length);

        Method listAll2 = Generic.class.getDeclaredMethod("listAll2");
        Class[] parameterTypes5 = GenericUtils.resolveTypeParameters(Generic.class, listAll2.getGenericReturnType());
        Assert.assertEquals(1, parameterTypes5.length);
        Assert.assertEquals(Object.class, parameterTypes5[0]);

        Method getMap = Generic.class.getDeclaredMethod("getMap");
        Class[] parameterTypes6 = GenericUtils.resolveTypeParameters(Generic.class, getMap.getGenericReturnType());
        Assert.assertEquals(2, parameterTypes6.length);
        Assert.assertEquals(Integer.class, parameterTypes6[0]);
        Assert.assertEquals(People.class, parameterTypes6[1]);

        Method getSet = Generic.class.getDeclaredMethod("getSet");
        Class[] parameterTypes7 = GenericUtils.resolveTypeParameters(Generic.class, getSet.getGenericReturnType());
        Assert.assertEquals(1, parameterTypes7.length);
        Assert.assertEquals(Integer.class, parameterTypes7[0]);
    }

    interface Generic {
        List<Date> justTest(Object target);

        People findOne(String id);

        List<People> listAll(String name);

        People[] listArr(String name);

        Class<?> listAll2();

        Map<Integer, People> getMap();

        Set<Integer> getSet();
    }

    @Data
    @AllArgsConstructor
    class People {
        private String name;
        private Integer age;
        private String id;
        private Date birthday;
        private String address;
    }
}
