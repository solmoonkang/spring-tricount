package kr.co.springtricount.infra.handler;

import kr.co.springtricount.infra.exception.*;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    protected ResponseEntity<ResponseFormat<Void>> handleInvalidRequestException(InvalidRequestException e) {
        log.warn("INVALID REQUEST: ", e);

        ResponseFormat<Void> responseFormat =
                ResponseFormat.failureMessage(ResponseStatus.FAIL_BAD_REQUEST, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseFormat);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    protected ResponseEntity<ResponseFormat<Void>> handleAuthenticationException(UnauthorizedAccessException e) {
        log.warn("AUTHENTICATION REQUEST: ", e);

        ResponseFormat<Void> responseFormat =
                ResponseFormat.failureMessage(ResponseStatus.FAIL_UNAUTHORIZED, e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseFormat);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseFormat<Void>> handleNotFoundException(NotFoundException e) {
        log.warn("NOT FOUND REQUEST: ", e);

        ResponseFormat<Void> responseFormat =
                ResponseFormat.failureMessage(ResponseStatus.FAIL_NOT_FOUND, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseFormat);
    }

    @ExceptionHandler(DuplicatedException.class)
    protected ResponseEntity<ResponseFormat<Void>> handleDuplicatedException(DuplicatedException e) {
        log.warn("DUPLICATED REQUEST: ", e);

        ResponseFormat<Void> responseFormat =
                ResponseFormat.failureMessage(ResponseStatus.FAIL_DUPLICATED, e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseFormat);
    }

    @ExceptionHandler(BusinessLogicException.class)
    protected ResponseEntity<ResponseFormat<Void>> handleBusinessLogicException(BusinessLogicException e) {
        log.warn("RUNTIME EXCEPTION: ", e);

        ResponseFormat<Void> responseFormat =
                ResponseFormat.failureMessage(ResponseStatus.FAIL_SERVER_ERROR, e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseFormat);
    }
}
