package kr.co.springtricount.infra.error.exception;

import kr.co.springtricount.infra.error.response.ResponseStatus;

public class LoginProcessException extends BusinessLogicException {

	public LoginProcessException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public LoginProcessException(String message) {
		super(message);
	}
}
