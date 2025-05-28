package exception;

public class SQLAbfrageFehlgeschlagenException extends RuntimeException {
    public SQLAbfrageFehlgeschlagenException(Throwable cause) {
        super("SQL-Abfrage fehlgeschlagen", cause);
    }
}
