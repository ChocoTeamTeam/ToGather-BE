package chocoteamteam.togather.exception;


public class MemberException extends RuntimeException {

	public MemberException() {
		super();
	}

	public MemberException(String message) {
		super(message);
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
	}

}
