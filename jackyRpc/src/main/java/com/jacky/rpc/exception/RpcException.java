package com.jacky.rpc.exception;

/**
 * @Author: jacky
 * @Date:2024/2/22 18:41
 * @Description: rpc调用异常
 **/
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 6922970840107066104L;

    private String errorCode;

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
