package org.oasis.easy.orm.conf.core.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.oasis.easy.orm.conf.core.ServiceRuntime;
import org.oasis.easy.orm.conf.model.Signer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryServiceRuntime extends ServiceRuntime {

    @Override
    protected List<Object> fillBizValues() {
        List<Object> objects = createObjects();
        Map<String, Object> bizValueMap = getBizValueMap();
        Signer signer = (Signer) objects.get(1);
        signer.setId(Objects.toString(bizValueMap.get("id")));
        signer.setImage(Objects.toString(bizValueMap.get("image")));
        return objects;
    }

    @Override
    protected Boolean parseResult(Object response) {
        return NumberUtils.toInt(response.toString()) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
