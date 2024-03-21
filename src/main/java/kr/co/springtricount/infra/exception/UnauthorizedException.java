package kr.co.springtricount.infra.exception;

public class UnauthorizedException extends BusinessLogicException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
