package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class AuthenticationException extends BusinessLogicException {

    public AuthenticationException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
