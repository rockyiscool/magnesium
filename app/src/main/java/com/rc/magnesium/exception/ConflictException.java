package com.rc.magnesium.exception;

import org.springframework.http.HttpStatus;

public abstract class ConflictException extends ApplicationException {

    private static final long serialVersionUID = 2732387272497796271L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    protected ConflictException(int moduleCode, int itemCode, String message) {
        super(moduleCode, itemCode, HTTP_STATUS, message);
    }
}
