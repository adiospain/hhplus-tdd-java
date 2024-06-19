package io.hhplus.tdd.point.exception;

import io.hhplus.tdd.exception.CustomException;
import io.hhplus.tdd.exception.ErrorCode;

public class PointException extends CustomException {
    public PointException (ErrorCode errorCode){
        super(errorCode);
    }
    public PointException(ErrorCode errorCode,
                          String runtimeValue){
        super(errorCode, runtimeValue);
    }
}
