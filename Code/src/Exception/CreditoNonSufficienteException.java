package Exception;

public class CreditoNonSufficienteException extends Exception {

    public CreditoNonSufficienteException() {
        super("Credito insufficiente");
    }

    public CreditoNonSufficienteException(String messaggio) {
        super(messaggio);
    }
    
}
