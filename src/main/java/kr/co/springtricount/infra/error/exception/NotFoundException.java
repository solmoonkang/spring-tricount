package kr.co.springtricount.infra.error.exception;

import kr.co.springtricount.infra.error.response.ResponseStatus;

public class NotFoundException extends BusinessLogicException {

	public NotFoundException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public NotFoundException(String message) {
		super(message);
	}
}
