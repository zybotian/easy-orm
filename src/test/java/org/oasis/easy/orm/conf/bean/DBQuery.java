package org.oasis.easy.orm.conf.bean;

import org.oasis.easy.orm.conf.model.Signer;

// 第三方接口统一封装在这里
// TODO 增加对方法名重复的支持
// TODO 增加对参数对象是复杂对象的支持
public class DBQuery {

    public Object query(String code, Signer signer) {
        return "kb_coupon_package".equals(code)
                && "signStatus".equals(signer.getType())
                && "208810091000".equals(signer.getId()) ? 0 : -1;
    }

    public Boolean checkCode(String code) {
        return "kb_coupon_package".equals(code);
    }

    public String checkSigner(Signer signer) {
        return "signStatus".equals(signer.getType()) && "208810091000".equals(signer.getId()) ? "SUCCESS" : "ERROR";
    }
}
