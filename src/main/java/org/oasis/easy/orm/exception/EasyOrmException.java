package org.oasis.easy.orm.exception;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmException extends RuntimeException {

    private final ErrorCode errorCode;

    private String errorMessage = "";

    public EasyOrmException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public EasyOrmException(ErrorCode errorCode, String errorMessage) {
        this(errorCode);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "EasyOrmException{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
