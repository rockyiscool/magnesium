package com.rc.magnesium.exception;

import com.rc.magnesium.payload.response.v1.BadRequestResponse;
import com.rc.magnesium.payload.response.v1.ErrorResponse;
import com.rc.magnesium.payload.response.v1.GeneralErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<BadRequestResponse.InvalidField> invalidFields = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new BadRequestResponse.InvalidField()
                        .setName(fieldError.getField())
                        .setMessage(fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        BadRequestResponse dto = new BadRequestResponse()
                .setInvalidFields(invalidFields);

        return buildErrorResponse(dto, HttpStatus.BAD_REQUEST, "0x000001", exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        return buildErrorResponse(new GeneralErrorResponse(), exception.getHttpStatus(), exception.getErrorCode(), exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        return buildErrorResponse(new GeneralErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR, "0x999999", exception);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorResponse dto, HttpStatus status, String errorCode, Exception exception) {
        String errorMessage = String.format("Exception type: %s, exception message: %s",
                exception.getClass(),
                exception.getMessage());

        logger.error("Error code: {}", errorCode, exception);

        String traceId = MDC.get("X-B3-TraceId");
        String spanId = MDC.get("X-B3-SpanId");
        dto.setTraceId(traceId);
        dto.setSpanId(spanId);
        dto.setCode(errorCode);
        dto.setMessage(errorMessage);

        return new ResponseEntity<>(dto, new HttpHeaders(), status);
    }
}