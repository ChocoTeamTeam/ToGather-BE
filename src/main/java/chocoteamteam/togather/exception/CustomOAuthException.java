package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class CustomOAuthException extends RuntimeException {

    private final ErrorCode errorCode;
    private final int status;
    private final String errorMessage;

    public CustomOAuthException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }

}
