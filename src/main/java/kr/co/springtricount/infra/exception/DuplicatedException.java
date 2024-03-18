package kr.co.springtricount.infra.exception;

public class DuplicatedException extends BusinessLogicException {

    public DuplicatedException(String message) {
        super(message);
    }
}
