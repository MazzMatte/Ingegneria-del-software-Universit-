package Exception;

public class CartaNonValidaException extends Exception{
    
    public CartaNonValidaException() {
        super("Carta di credito inesistente (deve avere 16 cifre)!");
    }

    public CartaNonValidaException(String messaggio) {
        super(messaggio);
    }
    
}
