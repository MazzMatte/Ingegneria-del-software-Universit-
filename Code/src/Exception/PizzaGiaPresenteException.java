package Exception;

public class PizzaGiaPresenteException extends Exception{
    
     public PizzaGiaPresenteException() {
        super("Pizza gia' presente nel menu");
    }

    public PizzaGiaPresenteException(String messaggio) {
        super(messaggio);
    }
    
}
