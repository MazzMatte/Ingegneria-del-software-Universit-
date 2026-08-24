package Exception;

public class CapNonValidoException extends Exception {
    
    public CapNonValidoException(){
        super("CAP non valida ( deve avere 5 cifre ).");
    }

    public CapNonValidoException(String messaggio) {
        super(messaggio);
    }
}