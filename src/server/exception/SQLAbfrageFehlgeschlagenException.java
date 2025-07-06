package server.exception;

/**
 * Eigene Unchecked Exception. In den Data Acces Objekten werden SQL Exceptions abgefangen
 * und im Gegenzug diese Exception geworfen. Daher weiß man genau, dass etwas
 * beim Kontakt mit der Datenbank fehlgeschlagen ist.
 */
public class SQLAbfrageFehlgeschlagenException extends RuntimeException {
    public SQLAbfrageFehlgeschlagenException(Throwable cause) {
        super("SQL-Abfrage fehlgeschlagen: ", cause);
    }
}
