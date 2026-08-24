package Exception;

public class UtenteGiaPresenteException extends Exception{
    
    public UtenteGiaPresenteException() {
        super("Utente presente ");
    }

    public UtenteGiaPresenteException(String messaggio) {
        super(messaggio);
    }
    
}
