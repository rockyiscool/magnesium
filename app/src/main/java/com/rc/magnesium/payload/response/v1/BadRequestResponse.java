package com.rc.magnesium.payload.response.v1;

import java.util.List;

public class BadRequestResponse extends ErrorResponse<BadRequestResponse> {

    private List<InvalidField> invalidFields;

    public List<InvalidField> getInvalidFields() {
        return invalidFields;
    }

    public BadRequestResponse setInvalidFields(List<InvalidField> invalidFields) {
        this.invalidFields = invalidFields;
        return this;
    }

    public static class InvalidField {

        private String name;
        private String message;

        public String getName() {
            return name;
        }

        public InvalidField setName(String name) {
            this.name = name;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public InvalidField setMessage(String message) {
            this.message = message;
            return this;
        }
    }
}
