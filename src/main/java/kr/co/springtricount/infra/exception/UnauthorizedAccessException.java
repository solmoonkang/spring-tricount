package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class UnauthorizedAccessException extends BusinessLogicException {

	public UnauthorizedAccessException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
