package kr.co.springtricount.infra.exception;

public class NotFoundException extends BusinessLogicException {

    public NotFoundException(String message) {
        super(message);
    }
}
