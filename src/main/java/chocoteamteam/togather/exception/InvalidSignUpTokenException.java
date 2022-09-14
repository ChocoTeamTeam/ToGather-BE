package chocoteamteam.togather.exception;


public class InvalidSignUpTokenException extends RuntimeException {

	public InvalidSignUpTokenException() {
		super();
	}

	public InvalidSignUpTokenException(String message) {
		super(message);
	}

	public InvalidSignUpTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSignUpTokenException(Throwable cause) {
		super(cause);
	}

	protected InvalidSignUpTokenException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
