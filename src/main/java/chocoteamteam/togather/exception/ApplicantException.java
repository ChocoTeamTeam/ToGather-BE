package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class ApplicantException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int status;
	private final String errorMessage;

	public ApplicantException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
		this.errorMessage = errorCode.getErrorMessage();
	}

}
