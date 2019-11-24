package org.oasis.easy.orm.conf.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDrm {

    public static String getServiceName() {
        return "org.oasis.easy.orm.conf.bean.DBQuery";
    }

    public static String getMethodName() {
        return "query";
    }

    public static List<String> getMethodParameterClasses() {
        return Arrays.asList("java.lang.String", "org.oasis.easy.orm.conf.model.Signer");
    }

    public static Map<String, Object> getMethodParameterValueMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("#0", "kb_coupon_package");
        map.put("#1", "{\n" +
                "  \"id\": null,\n" +
                "  \"type\": \"signStatus\",\n" +
                "  \"title\": \"中国上海\",\n" +
                "  \"image\": null\n" +
                "}");
        return map;
    }

    // 新增一个接口
    public static Map<String, Object> getMethodParameterValueMapForCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("#0", "kb_coupon_package");
        return map;
    }

    public static String getMethodNameForCode() {
        return "checkCode";
    }

    public static List<String> getMethodParameterClassesForCode() {
        return Arrays.asList("java.lang.String");
    }

    // 新增一个接口
    public static Map<String, Object> getMethodParameterValueMapForSigner() {
        Map<String, Object> map = new HashMap<>();
        map.put("#0", "{\n" +
                "  \"id\": null,\n" +
                "  \"type\": \"signStatus\",\n" +
                "  \"title\": \"中国上海\",\n" +
                "  \"image\": \"img_0017.jpg\"\n" +
                "}");
        return map;
    }

    public static String getMethodNameForSigner() {
        return "checkSigner";
    }

    public static List<String> getMethodParameterClassesForSigner() {
        return Arrays.asList("org.oasis.easy.orm.conf.model.Signer");
    }

}
