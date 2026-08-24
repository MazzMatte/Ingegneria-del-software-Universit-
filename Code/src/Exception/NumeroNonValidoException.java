package Exception;

public class NumeroNonValidoException extends Exception{
    
    public NumeroNonValidoException(){
        super("Il numero telefonico deve contenere esattamente 10 cifre!");
    }

    public NumeroNonValidoException(String messaggio) {
        super(messaggio);
    }
    
}