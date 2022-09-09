package chocoteamteam.togather.exception;


public class JwtParseException extends RuntimeException {

	public JwtParseException() {
		super();
	}

	public JwtParseException(String message) {
		super(message);
	}

	public JwtParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JwtParseException(Throwable cause) {
		super(cause);
	}

	protected JwtParseException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
