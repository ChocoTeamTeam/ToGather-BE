package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int status;
	private final String errorMessage;

	public MemberException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
		this.errorMessage = errorCode.getErrorMessage();
	}

}
