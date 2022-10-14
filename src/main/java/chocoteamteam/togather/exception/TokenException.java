package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int status;
    private final String errorMessage;

    public TokenException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public TokenException(ErrorCode errorCode,Throwable e) {
        super(errorCode.getErrorMessage(),e);
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }
}