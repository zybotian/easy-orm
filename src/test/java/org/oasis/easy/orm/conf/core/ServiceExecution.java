package org.oasis.easy.orm.conf.core;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Service
public class ServiceExecution {

    public Boolean execute(ServiceRuntime serviceRuntime) throws Exception {
        Object serviceProxy = serviceRuntime.getServiceProxy();
        Method method = serviceProxy.getClass().getMethod(serviceRuntime.getMethodName(), serviceRuntime.getParameterTypeList());
        Object response = ReflectionUtils.invokeMethod(method, serviceProxy, serviceRuntime.getArgs());
        return serviceRuntime.parseResult(response);
    }
}
