package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class ProjectException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int status;
    private final String errorMessage;

    public ProjectException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }
}