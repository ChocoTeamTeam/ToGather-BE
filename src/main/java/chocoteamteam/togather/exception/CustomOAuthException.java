package chocoteamteam.togather.exception;

public class CustomOAuthException extends RuntimeException {

    public CustomOAuthException() {
    }

    public CustomOAuthException(String message) {
        super(message);
    }

    public CustomOAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
