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
    MISSING_PARAM(101006, "missing param", "参数缺失"),
    CONFIG_ERROR(101007, "config error", "配置错误"),
    SERVICE_ERROR(101008, "internal service error", "内部服务错误"),
    EMPTY_DATA_SET_ERROR(101009, "empty data set error", "空的数据集"),
    INCORRECT_DATA_SIZE_ERROR(101010, "incorrect data size error", "数据集的size不正确"),
    INCORRECT_DATA_TYPE_ERROR(101011, "incorrect data type error", "数据类型不正确"),
    MAPPING_ERROR(101012, "mapping error", "映射错误"),


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
