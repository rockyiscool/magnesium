package com.rc.magnesium.payload.response.v1;

public abstract class ErrorResponse<T> {

    private String traceId;
    private String spanId;
    private String code;
    private String message;

    protected ErrorResponse() {
    }

    public String getTraceId() {
        return traceId;
    }

    public T setTraceId(String traceId) {
        this.traceId = traceId;
        return (T) this;
    }

    public String getSpanId() {
        return spanId;
    }

    public T setSpanId(String spanId) {
        this.spanId = spanId;
        return (T) this;
    }

    public String getCode() {
        return code;
    }

    public T setCode(String code) {
        this.code = code;
        return (T) this;
    }

    public String getMessage() {
        return message;
    }

    public T setMessage(String message) {
        this.message = message;
        return (T) this;
    }
}
