package Exception;

public class QuantitaNonValidaException extends Exception{
    
    public QuantitaNonValidaException() {
        super("Quantita deve esserre un numero positivo!");
    }

    public QuantitaNonValidaException(String messaggio) {
        super(messaggio);
    }
    
}
