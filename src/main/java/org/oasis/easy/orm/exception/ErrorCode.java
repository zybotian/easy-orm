package org.oasis.easy.orm.exception;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public enum ErrorCode {
    SUCCESS(100000, "success", "成功"),

    // 配置相关
    MISSING_CONFIG(101001, "missing config", "缺失配置"),
    IO_ERROR(101002, "IO error", "IO异常"),
    OBJECT_CREATING_ERROR(101003, "object create error", "对象创建异常"),
    UNSUPPORTED_OPERATION_ERROR(101004, "unsupported operation error", "不支持的操作"),
    INVALID_PARAM(101005, "invalid param", "无效参数"),
    MISSING_PARAM(101005, "missing param", "参数缺失"),


    // 其他
    ;

    private Integer code;
    private String msg;
    private String desc;

    ErrorCode(Integer code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesc() {
        return desc;
    }
}
