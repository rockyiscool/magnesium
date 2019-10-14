package com.rc.magnesium.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 8047988215813505755L;

    protected int moduleCode = 0;
    protected int itemCode = 0;
    protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    protected ApplicationException(int moduleCode, int itemCode, HttpStatus httpStatus, String message) {
        super(message);
        setup(moduleCode, itemCode, httpStatus);
    }

    protected ApplicationException(int moduleCode, int itemCode, HttpStatus httpStatus, String message,
                                   Throwable cause) {
        super(message, cause);
        setup(moduleCode, itemCode, httpStatus);
    }

    private void setup(int moduleCode, int itemCode, HttpStatus httpStatus) {
        this.moduleCode = moduleCode;
        this.itemCode = itemCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return String.format("0x%1$03d%2$03d", moduleCode, itemCode);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
