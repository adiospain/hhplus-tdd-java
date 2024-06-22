package io.hhplus.tdd.exception;


public class CustomException extends RuntimeException{
    private final ErrorResponse errorResponse;

    public CustomException(ErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
