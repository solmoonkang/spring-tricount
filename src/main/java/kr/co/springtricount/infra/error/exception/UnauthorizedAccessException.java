package kr.co.springtricount.infra.error.exception;

import kr.co.springtricount.infra.error.response.ResponseStatus;

public class UnauthorizedAccessException extends BusinessLogicException {

	public UnauthorizedAccessException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
