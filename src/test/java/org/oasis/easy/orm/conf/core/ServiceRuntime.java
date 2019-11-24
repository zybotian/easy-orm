package org.oasis.easy.orm.conf.core;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

public abstract class ServiceRuntime {

    private Object serviceProxy;

    private String methodName;

    private Class<?>[] parameterTypeList;

    private Map<String, Object> configuredValueMap;

    private Map<String, Object> bizValueMap;

    protected List<Object> createObjects() {
        if (ArrayUtils.getLength(parameterTypeList) == 0) {
            return Lists.newArrayList();
        }

        List<Object> objects = Lists.newArrayList();

        for (Class<?> clazz : parameterTypeList) {
            objects.add(BeanUtils.instantiate(clazz));
        }

        return objects;
    }

    protected List<Object> fillConfigValues() {
        List<Object> objects = Lists.newArrayList();

        for (int i = 0; i < parameterTypeList.length; i++) {
            String paramKey = "#" + i;
            if (BeanUtils.isSimpleProperty(parameterTypeList[i])) {
                objects.add(configuredValueMap.get(paramKey));
            } else {
                // 从配置中获取数据
                Object object = JSONObject.parseObject(configuredValueMap.get(paramKey).toString(), parameterTypeList[i]);
//                // 将配置和业务数据合并，若同时存在则配置优先级高
//                Object instance = BeanUtils.instantiate(objects.get(i).getClass());
//                BeanUtils.copyProperties(objects.get(i), instance);
//                // 配置优先级高
//                BeanUtils.copyProperties(object, instance);
//                objects.add(instance);
                objects.add(object);
            }
        }

        return objects;
    }

    /**
     * 实现类需要告诉父类业务逻辑参数
     *
     * @return
     */
    protected abstract List<Object> fillBizValues();

    /**
     * 解析接口调用结果，子类可根据实际情况决定是否需要覆盖
     *
     * @param response
     * @return
     */
    protected Boolean parseResult(Object response) {
        if (response == null) {
            return Boolean.FALSE;
        }
        return response == null ? Boolean.FALSE : Boolean.valueOf(response.toString());
    }

    /**
     * 构造参数值
     *
     * @throws Exception
     */
    public Object[] getArgs() {
        // 填充业务参数
        List<Object> argsByBiz = fillBizValues();
        // 填充配置参数
        List<Object> argsByConfig = fillConfigValues();
        return mergeList(argsByBiz, argsByConfig).toArray();
    }

    private List<Object> mergeList(List<Object> argsByBiz, List<Object> argsByConfig) {
        List<Object> result = Lists.newArrayList();
        for (int i = 0; i < parameterTypeList.length; i++) {
            if (BeanUtils.isSimpleValueType(parameterTypeList[i])) {
                if (argsByBiz.get(i) == null && argsByConfig.get(i) == null) {
                    throw new RuntimeException("biz params or config params invalid");
                }
                if (argsByBiz.get(i) != null || argsByConfig.get(i) != null) {
                    String biz = argsByBiz.get(i).toString();
                    String config = argsByConfig.get(i).toString();
                    result.add(StringUtils.isNotEmpty(config) ? config : biz);
                } else {
                    result.add(argsByConfig.get(i) != null ? argsByConfig.get(i) : argsByBiz.get(i));
                }
            } else {
                if (argsByBiz.get(i) != null && argsByConfig.get(i) != null) {
                    result.add(mergeObject(argsByBiz.get(i), argsByConfig.get(i)));
                }
            }
        }
        return result;
    }

    protected Object mergeObject(Object bizObj, Object confObj) {
        Object result = BeanUtils.instantiate(bizObj.getClass());
        BeanUtils.copyProperties(bizObj, result, getNullPropertyNames(bizObj));
        BeanUtils.copyProperties(confObj, result, getNullPropertyNames(confObj));
        return result;
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    public void setParameterTypeList(Class<?>[] parameterTypeList) {
        this.parameterTypeList = parameterTypeList;
    }

    public Class<?>[] getParameterTypeList() {
        return parameterTypeList;
    }

    public void setConfiguredValueMap(Map<String, Object> configuredValueMap) {
        this.configuredValueMap = configuredValueMap;
    }

    public void setBizValueMap(Map<String, Object> bizValueMap) {
        this.bizValueMap = bizValueMap;
    }

    protected Map<String, Object> getBizValueMap() {
        return bizValueMap;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getServiceProxy() {
        return serviceProxy;
    }

    public void setServiceProxy(Object serviceProxy) {
        this.serviceProxy = serviceProxy;
    }
}
