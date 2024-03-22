package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class LoginException extends BusinessLogicException {

    public LoginException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public LoginException(String message) {
        super(message);
    }
}
