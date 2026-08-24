package Entity;

/**
 * Rappresenta un gestore del sistema Pizza@Casa.
 * Estende la classe EntityUtente con le credenziali base (email e password).
 */
public class EntityGestore extends EntityUtente {

    /**
     * Costruttore parametrizzato per creare un oggetto EntityGestore.
     *
     * @param email    Email del gestore (identificativo utente)
     * @param password Password del gestore
     */
    public EntityGestore(String email, String password) {
        super(email, password);
    }
}
