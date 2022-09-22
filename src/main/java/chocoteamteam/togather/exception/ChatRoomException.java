package chocoteamteam.togather.exception;

import lombok.Getter;

@Getter
public class ChatRoomException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int status;
	private final String errorMessage;

	public ChatRoomException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
		this.errorMessage = errorCode.getErrorMessage();
	}

}
