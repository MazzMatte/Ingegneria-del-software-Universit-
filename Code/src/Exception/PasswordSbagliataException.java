package Exception;

public class PasswordSbagliataException extends Exception{
    
    public PasswordSbagliataException() {
        super("Password Errata!");
    }

    public PasswordSbagliataException(String messaggio) {
        super(messaggio);
    }
    
}
