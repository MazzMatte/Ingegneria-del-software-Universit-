package Entity;

/**
 * Rappresenta un Rider, estensione di EntityUtente,
 * con informazioni aggiuntive specifiche per il rider come nome, cognome,
 * CAP di lavoro e stato (libero o impegnato).
 */
public class EntityRider extends EntityUtente {
    private String nome;
    private String cognome;
    private String CAPDiLavoro;
    private String stato; // può essere "libero" o "impegnato";

    /**
     * Costruttore parametrizzato per creare un oggetto EntityRider.
     * 
     * @param email email del rider (ereditata da EntityUtente)
     * @param password password del rider (ereditata da EntityUtente)
     * @param nome nome del rider
     * @param cognome cognome del rider
     * @param CAPDiLavoro CAP dove il rider svolge la consegna
     * @param stato stato del rider ("libero" o "impegnato")
     */
    public EntityRider(String email, String password, String nome, String cognome, String CAPDiLavoro, String stato) {
        super(email, password);
        this.nome = nome; 
        this.cognome = cognome;
        this.CAPDiLavoro = CAPDiLavoro;
        this.stato = stato; // inizializzato a "libero"... diventa "occupato" se sta lavorando ad un ordine
    }

//METODI GETT----------------------------------------------------------------
    /**
     * Restituisce il nome del rider.
     * 
     * @return nome del rider
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce il cognome del rider.
     * 
     * @return cognome del rider
     */
    public String getCognome() {
        return this.cognome;
    }

    /**
     * Restituisce il CAP dove il rider lavora.
     * 
     * @return CAP di lavoro del rider
     */
    public String getCAPDiLavoro() {
        return this.CAPDiLavoro;
    }

    /**
     * Restituisce lo stato attuale del rider ("libero" o "impegnato").
     * 
     * @return stato del rider
     */
    public String getStato() {
        return this.stato;
    }

//METODI SET----------------------------------------------------------------
    /**
     * Imposta il nome del rider.
     * 
     * @param nome nuovo nome del rider
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome del rider.
     * 
     * @param cognome nuovo cognome del rider
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta il CAP di lavoro del rider.
     * 
     * @param CAPDiLavoro nuovo CAP di lavoro
     */
    public void setCAPDiLavoro(String CAPDiLavoro) {
        this.CAPDiLavoro = CAPDiLavoro;
    }

    /**
     * Imposta lo stato del rider.
     * 
     * @param stato nuovo stato ("libero" o "impegnato")
     */
    public void setStato(String stato) {
        this.stato = stato;
    }
}
