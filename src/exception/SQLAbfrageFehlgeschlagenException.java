package exception;

public class SQLAbfrageFehlgeschlagenException extends RuntimeException {
    public SQLAbfrageFehlgeschlagenException(Exception message) {
        super(message);
    }
}
