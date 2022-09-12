package chocoteamteam.togather.exception;


public class InvalidRefreshTokenException extends RuntimeException {

	public InvalidRefreshTokenException() {
		super();
	}

	public InvalidRefreshTokenException(String message) {
		super(message);
	}

	public InvalidRefreshTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
