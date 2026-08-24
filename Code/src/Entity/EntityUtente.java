package Entity;

/**
 * Classe base che rappresenta un utente generico con email e password.
 */
public class EntityUtente {
    private String email;
    private String password;

    /**
     * Costruttore con parametri per creare un utente con email e password.
     *
     * @param email Email dell'utente (identificativo univoco).
     * @param password Password associata all'utente.
     */
    public EntityUtente(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    /**
     * Imposta l'email dell'utente.
     * @param email Email da impostare.
     */
    public void setEmail(String email){
        this.email = email;
    }
    
    /**
     * Imposta la password dell'utente.
     * @param password Password da impostare.
     */
    public void setPassword(String password){
        this.password = password;
    }
    
    /**
     * Restituisce l'email dell'utente.
     * @return Email dell'utente.
     */
    public String getEmail(){
        return this.email;
    }
    
    /**
     * Restituisce la password dell'utente.
     * @return Password dell'utente.
     */
    public String getPassword(){
        return this.password;
    }
}
