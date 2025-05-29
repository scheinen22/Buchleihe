package exception;

public class BuchNichtGefundenException extends RuntimeException {

    public BuchNichtGefundenException() {
        super("Das gesuchte Buch wurde nicht gefunden.");
    }

    public BuchNichtGefundenException(String message) {
        super(message);
    }

    public BuchNichtGefundenException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuchNichtGefundenException(Throwable cause) {
        super(cause);
    }
}
