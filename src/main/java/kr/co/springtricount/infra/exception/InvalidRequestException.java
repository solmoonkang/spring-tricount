package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class InvalidRequestException extends BusinessLogicException {

    public InvalidRequestException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
