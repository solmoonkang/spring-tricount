package kr.co.springtricount.infra.error.exception;

import kr.co.springtricount.infra.error.response.ResponseStatus;

public class DuplicatedException extends BusinessLogicException {

	public DuplicatedException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public DuplicatedException(String message) {
		super(message);
	}
}
