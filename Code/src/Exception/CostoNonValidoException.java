package Exception;

public class CostoNonValidoException extends Exception{
    
    public CostoNonValidoException() {
        super("Costo non valido (es. Costo > 0");
    }

    public CostoNonValidoException(String messaggio) {
        super(messaggio);
    }
    
}
