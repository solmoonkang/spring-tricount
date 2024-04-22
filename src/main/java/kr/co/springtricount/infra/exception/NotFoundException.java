package kr.co.springtricount.infra.exception;

import kr.co.springtricount.infra.response.ResponseStatus;

public class NotFoundException extends BusinessLogicException {

	public NotFoundException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public NotFoundException(String message) {
		super(message);
	}
}
