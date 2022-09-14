package chocoteamteam.togather.exception;


public class MemberStatusException extends RuntimeException {

	public MemberStatusException() {
		super();
	}

	public MemberStatusException(String message) {
		super(message);
	}

	public MemberStatusException(String message, Throwable cause) {
		super(message, cause);
	}

}
