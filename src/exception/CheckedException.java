package exception;

/**
 * Eigene Checked Exception. Wird an Stellen geworfen, in denen mit einem Fehler durch den Nutzerinput
 * oder durch die Geschäftslogik limitierte Vorgänge gerechnet wird.
 * @author Said Halilovic
 */
public class CheckedException extends Exception {
    public CheckedException(String message) {
        super(message);
    }
}
