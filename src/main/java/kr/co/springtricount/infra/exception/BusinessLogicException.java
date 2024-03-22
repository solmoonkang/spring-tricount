package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
    }

    public BusinessLogicException(String message) {
        super(message);
    }
}
