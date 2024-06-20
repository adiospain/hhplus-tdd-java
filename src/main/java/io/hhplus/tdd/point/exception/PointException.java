package io.hhplus.tdd.point.exception;

import io.hhplus.tdd.exception.CustomException;
import io.hhplus.tdd.exception.ErrorResponse;

public class PointException extends CustomException {
    public PointException (ErrorResponse errorResponse) {super (errorResponse);}
}
