
package com.sus.lefteyecinema.utils;


/**
 * 自定义异常
 * 
 */
public class CustomException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int errorCode;

    private Exception exception;

    public CustomException() {
        super();
    }

    public CustomException(int errorCode, Exception exception) {
        super();
        this.errorCode = errorCode;
        this.exception = exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Exception getException() {
    	if(exception == null){
    		return new Exception("unknown null exception");
    	}
        return exception;
    }

}
