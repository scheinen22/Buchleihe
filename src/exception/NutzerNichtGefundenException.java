package exception;

public class NutzerNichtGefundenException extends Exception {
    public NutzerNichtGefundenException(Throwable cause) {
        super("Nutzer nicht gefunden: ", cause);
    }
}
