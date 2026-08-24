package Exception;

public class NumeroCivicoNonValidoException extends Exception{
    
    public NumeroCivicoNonValidoException() {
        super("Il numero civico deve contenere almeno una cifra.");
    }

    public NumeroCivicoNonValidoException(String messaggio) {
        super(messaggio);
    }
    
}
