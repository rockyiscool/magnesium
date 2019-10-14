package com.rc.magnesium.exception;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends ApplicationException {

    private static final long serialVersionUID = 7393977090141139720L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    protected NotFoundException(int moduleCode, int itemCode, String message) {
        super(moduleCode, itemCode, HTTP_STATUS, message);
    }
}
