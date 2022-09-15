package chocoteamteam.togather.exception;

public class TechStackException extends RuntimeException {

    public TechStackException() {
    }

    public TechStackException(String message) {
        super(message);
    }

    public TechStackException(String message, Throwable cause) {
        super(message, cause);
    }
}
