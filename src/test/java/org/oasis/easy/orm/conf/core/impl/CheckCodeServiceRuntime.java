package org.oasis.easy.orm.conf.core.impl;

import org.oasis.easy.orm.conf.core.ServiceRuntime;

import java.util.List;

public class CheckCodeServiceRuntime extends ServiceRuntime {

    @Override
    protected List<Object> fillBizValues() {
        // 无业务参数
        return createObjects();
    }
}
