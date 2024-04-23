package kr.co.springtricount.infra.error.exception;

import kr.co.springtricount.infra.error.response.ResponseStatus;

public class InvalidRequestException extends BusinessLogicException {

	public InvalidRequestException(ResponseStatus responseStatus) {
		super(responseStatus);
	}

	public InvalidRequestException(String message) {
		super(message);
	}
}
