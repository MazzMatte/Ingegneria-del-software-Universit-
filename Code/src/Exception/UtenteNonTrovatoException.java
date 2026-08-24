package Exception;

public class UtenteNonTrovatoException extends Exception {
    
    public UtenteNonTrovatoException() {
        super("Utente non registrato!");
    }

    public UtenteNonTrovatoException (String messaggio) {
        super(messaggio);
    }
    
}
