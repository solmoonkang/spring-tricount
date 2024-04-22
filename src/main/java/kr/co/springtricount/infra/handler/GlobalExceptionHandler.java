package kr.co.springtricount.infra.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.springtricount.infra.exception.BusinessLogicException;
import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.infra.exception.InvalidRequestException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidRequestException.class)
	protected ResponseEntity<ResponseFormat<Void>> handleInvalidRequestException(InvalidRequestException e) {
		log.warn("INVALID REQUEST EXCEPTION OCCURRED: {}", e.getMessage());

		ResponseFormat<Void> responseFormat =
			ResponseFormat.failureMessage(ResponseStatus.FAIL_BAD_REQUEST, e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseFormat);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	protected ResponseEntity<ResponseFormat<Void>> handleAuthenticationException(UnauthorizedAccessException e) {
		log.warn("UNAUTHORIZED ACCESS EXCEPTION DETECTED: {}", e.getMessage());

		ResponseFormat<Void> responseFormat =
			ResponseFormat.failureMessage(ResponseStatus.FAIL_UNAUTHORIZED, e.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseFormat);
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ResponseFormat<Void>> handleNotFoundException(NotFoundException e) {
		log.warn("NOT FOUND EXCEPTION ENCOUNTERED: {}", e.getMessage());

		ResponseFormat<Void> responseFormat =
			ResponseFormat.failureMessage(ResponseStatus.FAIL_NOT_FOUND, e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseFormat);
	}

	@ExceptionHandler(DuplicatedException.class)
	protected ResponseEntity<ResponseFormat<Void>> handleDuplicatedException(DuplicatedException e) {
		log.warn("DUPLICATED EXCEPTION RAISED: {}", e.getMessage());

		ResponseFormat<Void> responseFormat =
			ResponseFormat.failureMessage(ResponseStatus.FAIL_DUPLICATED, e.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(responseFormat);
	}

	@ExceptionHandler(BusinessLogicException.class)
	protected ResponseEntity<ResponseFormat<Void>> handleBusinessLogicException(BusinessLogicException e) {
		log.warn("BUSINESS LOGIC EXCEPTION OCCURRED: {}", e.getMessage());

		ResponseFormat<Void> responseFormat =
			ResponseFormat.failureMessage(ResponseStatus.FAIL_SERVER_ERROR, e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseFormat);
	}
}
