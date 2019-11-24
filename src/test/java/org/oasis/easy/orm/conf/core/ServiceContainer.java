package org.oasis.easy.orm.conf.core;

import org.oasis.easy.orm.conf.bean.DBQuery;
import org.oasis.easy.orm.conf.core.impl.CheckCodeServiceRuntime;
import org.oasis.easy.orm.conf.core.impl.CheckSignerServiceRuntime;
import org.oasis.easy.orm.conf.core.impl.QueryServiceRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceContainer {
    private Map<String, Object> beanMap = new HashMap<>();

    private Map<String, Class> contextMap = new HashMap<>();

    @Autowired
    private DBQuery dbQuery;

    @PostConstruct
    public void init() {
        initBeanMap();
        initContextMap();
    }

    /**
     * 一旦新增接口类，这里添加对应的bean的proxy
     */
    private void initBeanMap() {
        dbQuery = new DBQuery(); // 应当使用bean注入的方式获取代理类对象
        beanMap.put("org.oasis.easy.orm.conf.bean.DBQuery", dbQuery);
    }

    /**
     * 每次新增方法，这里添加对应的class对象
     */
    private void initContextMap() {
        contextMap.put("org.oasis.easy.orm.conf.bean.DBQuery#query", QueryServiceRuntime.class);
        contextMap.put("org.oasis.easy.orm.conf.bean.DBQuery#checkCode", CheckCodeServiceRuntime.class);
        contextMap.put("org.oasis.easy.orm.conf.bean.DBQuery#checkSigner", CheckSignerServiceRuntime.class);
    }

    public Object getBean(String key) {
        return beanMap.get(key);
    }

    public Class getServiceContext(String key) {
        return contextMap.get(key);
    }
}
