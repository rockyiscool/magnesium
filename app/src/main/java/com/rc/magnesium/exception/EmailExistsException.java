package com.rc.magnesium.exception;

public class EmailExistsException extends ConflictException {

    private static final long serialVersionUID = 4584203068633864855L;
    private static final int MODULE_CODE = 1;
    private static final int ITEM_CODE = 1;

    public EmailExistsException() {
        super(MODULE_CODE, ITEM_CODE, "Email is existence");
    }

    public EmailExistsException(String message) {
        super(MODULE_CODE, ITEM_CODE, message);
    }
}
