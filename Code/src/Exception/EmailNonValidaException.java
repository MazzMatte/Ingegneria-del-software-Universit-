package Exception;

public class EmailNonValidaException extends Exception {

    public EmailNonValidaException() {
        super("Email non valida (es. nome@dominio.it).");
    }

    public EmailNonValidaException(String messaggio) {
        super(messaggio);
    }
}
