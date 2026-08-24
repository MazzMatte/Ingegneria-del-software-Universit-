package Exception;

public class PizzaNonPresenteException extends Exception{
    
    public PizzaNonPresenteException() {
        super("Pizza non presente nel menu");
    }

    public PizzaNonPresenteException(String messaggio) {
        super(messaggio);
    }
    
}
