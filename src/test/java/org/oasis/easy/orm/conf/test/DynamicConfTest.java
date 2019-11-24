package org.oasis.easy.orm.conf.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.oasis.easy.orm.conf.core.ConfigDrm;
import org.oasis.easy.orm.conf.core.ServiceContainer;
import org.oasis.easy.orm.conf.core.ServiceRuntime;
import org.oasis.easy.orm.conf.core.ServiceExecution;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class DynamicConfTest {

    private ServiceContainer serviceContainer;

    private ServiceExecution serviceExecution;

    @Before
    public void setup() {
        serviceContainer = new ServiceContainer();
        serviceContainer.init();
        serviceExecution = new ServiceExecution();
    }

    @Test
    public void testQuery() throws Exception {
        // 服务类的全路径
        String serviceName = ConfigDrm.getServiceName();

        // 方法名字
        String methodName = ConfigDrm.getMethodName();

        // 方法参数的类型
        List<String> parameterClass = ConfigDrm.getMethodParameterClasses();

        // 方法参数类型的class对象
        List<Class<?>> parameterClazz = new ArrayList<>();
        for (String clazz : parameterClass) {
            parameterClazz.add(Class.forName(clazz));
        }
        Class<?>[] parameterTypes = parameterClazz.toArray(new Class<?>[0]);

        // 配置的参数取值Map
        Map<String, Object> configuredParams = ConfigDrm.getMethodParameterValueMap();

        // 每次请求负责填充业务参数
        Map<String, Object> dynamicParams = new HashMap<>();
        dynamicParams.put("id", "208810091000");
        dynamicParams.put("image", "/img/XsOKqVu.jpeg");

        // 生成service context
        ServiceRuntime serviceRuntime = (ServiceRuntime) serviceContainer.getServiceContext(serviceName + "#" + methodName).newInstance();
        serviceRuntime.setServiceProxy(serviceContainer.getBean(serviceName));
        serviceRuntime.setMethodName(methodName);
        serviceRuntime.setParameterTypeList(parameterTypes);
        serviceRuntime.setConfiguredValueMap(configuredParams);
        serviceRuntime.setBizValueMap(dynamicParams);

        // 通过serviceExecution执行
        Assert.assertTrue(serviceExecution.execute(serviceRuntime));

        Map<String, Object> dynamicParams1 = new HashMap<>();
        dynamicParams1.put("id", "208810091001");
        dynamicParams1.put("image", "/img/XsOKqVu.jpeg");
        serviceRuntime.setBizValueMap(dynamicParams1);
        Assert.assertFalse(serviceExecution.execute(serviceRuntime));
    }

    @Test
    public void testCheckCode() throws Exception {
        // 服务类的全路径
        String serviceName = ConfigDrm.getServiceName();

        // 方法名字
        String methodName = ConfigDrm.getMethodNameForCode();

        // 方法参数的类型
        List<String> parameterClass = ConfigDrm.getMethodParameterClassesForCode();

        // 方法参数类型的class对象
        List<Class<?>> parameterClazz = new ArrayList<>();
        for (String clazz : parameterClass) {
            parameterClazz.add(Class.forName(clazz));
        }
        Class<?>[] parameterTypes = parameterClazz.toArray(new Class<?>[0]);

        // 配置的参数取值Map
        Map<String, Object> configuredParams = ConfigDrm.getMethodParameterValueMapForCode();

        Map<String, Object> dynamicParams = new HashMap<>();
        dynamicParams.put("id", "208810091000");
        dynamicParams.put("image", "/img/XsOKqVu.jpeg");

        // 生成service context
        ServiceRuntime serviceRuntime = (ServiceRuntime) BeanUtils.instantiate(serviceContainer.getServiceContext(serviceName + "#" + methodName));
        serviceRuntime.setServiceProxy(serviceContainer.getBean(serviceName));
        serviceRuntime.setMethodName(methodName);
        serviceRuntime.setParameterTypeList(parameterTypes);
        serviceRuntime.setConfiguredValueMap(configuredParams);
        serviceRuntime.setBizValueMap(dynamicParams);

        // 通过serviceExecution执行
        Assert.assertTrue(serviceExecution.execute(serviceRuntime));
    }

    @Test
    public void testCheckSigner() throws Exception {
        // 服务类的全路径
        String serviceName = ConfigDrm.getServiceName();

        // 方法名字
        String methodName = ConfigDrm.getMethodNameForSigner();

        // 方法参数的类型
        List<String> parameterClass = ConfigDrm.getMethodParameterClassesForSigner();

        // 方法参数类型的class对象
        List<Class<?>> parameterClazz = new ArrayList<>();
        for (String clazz : parameterClass) {
            parameterClazz.add(Class.forName(clazz));
        }
        Class<?>[] parameterTypes = parameterClazz.toArray(new Class<?>[0]);

        // 配置的参数取值Map
        Map<String, Object> configuredParams = ConfigDrm.getMethodParameterValueMapForSigner();

        // TODO Map膨胀问题
        Map<String, Object> dynamicParams = new HashMap<>();
        dynamicParams.put("id", "208810091000");
        dynamicParams.put("image", "/img/XsOKqVu.jpeg");

        // 生成service context
        ServiceRuntime serviceRuntime = (ServiceRuntime) BeanUtils.instantiate(serviceContainer.getServiceContext(serviceName + "#" + methodName));
        serviceRuntime.setServiceProxy(serviceContainer.getBean(serviceName));
        serviceRuntime.setMethodName(methodName);
        serviceRuntime.setParameterTypeList(parameterTypes);
        serviceRuntime.setConfiguredValueMap(configuredParams);
        serviceRuntime.setBizValueMap(dynamicParams);

        // 通过serviceExecution执行
        Assert.assertTrue(serviceExecution.execute(serviceRuntime));

        Map<String, Object> dynamicParams1 = new HashMap<>();
        dynamicParams1.put("id", "208810091001");
        dynamicParams1.put("image", "/img/XsOKqVu.jpeg");
        serviceRuntime.setBizValueMap(dynamicParams1);

        Assert.assertFalse(serviceExecution.execute(serviceRuntime));
    }
}
