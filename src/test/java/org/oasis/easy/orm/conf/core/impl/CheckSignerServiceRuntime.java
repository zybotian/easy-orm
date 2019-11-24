package org.oasis.easy.orm.conf.core.impl;

import org.oasis.easy.orm.conf.core.ServiceRuntime;
import org.oasis.easy.orm.conf.model.Signer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckSignerServiceRuntime extends ServiceRuntime {

    @Override
    protected List<Object> fillBizValues() {
        List<Object> objects = createObjects();
        Map<String, Object> bizValueMap = getBizValueMap();
        Signer signer = (Signer) objects.get(0);
        signer.setId(Objects.toString(bizValueMap.get("id")));
        return objects;
    }

    @Override
    protected Boolean parseResult(Object response) {
        return "SUCCESS".equals(response.toString());
    }
}
