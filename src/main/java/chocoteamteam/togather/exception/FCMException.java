package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class FCMException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int status;
	private final String errorMessage;

	public FCMException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
		this.errorMessage = errorCode.getErrorMessage();
	}

	public FCMException(ErrorCode errorCode,Throwable e) {
		super(errorCode.getErrorMessage(),e);
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
		this.errorMessage = errorCode.getErrorMessage();
	}

}
