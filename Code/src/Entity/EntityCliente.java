package Entity;

/**
 * Rappresenta un cliente del sistema Pizza@Casa.
 * Estende la classe EntityUtente aggiungendo informazioni specifiche
 * come nome, cognome, indirizzo, numero di telefono, carta di credito e credito disponibile.
 */
public class EntityCliente extends EntityUtente {

    private String nome;
    private String cognome;
    private String numeroTelefonico;
    private String numeroCartaDiCredito;
    private double credito;
    private String via;
    private String citta;
    private String numeroCivico;
    private String CAP;

    /**
     * Costruttore parametrizzato per creare un oggetto EntityCliente.
     *
     * @param email                Email del cliente (ereditata da EntityUtente)
     * @param password             Password del cliente (ereditata da EntityUtente)
     * @param nome                 Nome del cliente
     * @param cognome              Cognome del cliente
     * @param numeroTelefonico     Numero di telefono del cliente
     * @param numeroCartaDiCredito Numero della carta di credito del cliente
     * @param credito              Credito disponibile del cliente
     * @param via                  Via dell'indirizzo del cliente
     * @param citta                Città dell'indirizzo del cliente
     * @param numeroCivico         Numero civico dell'indirizzo
     * @param CAP                  Codice di Avviamento Postale (CAP)
     */
    public EntityCliente(String email, String password, String nome, String cognome,
                         String numeroTelefonico, String numeroCartaDiCredito,
                         double credito, String via, String citta,
                         String numeroCivico, String CAP) {
        super(email, password);
        this.nome = nome;
        this.cognome = cognome;
        this.numeroTelefonico = numeroTelefonico;
        this.numeroCartaDiCredito = numeroCartaDiCredito;
        this.credito = credito;
        this.via = via;
        this.citta = citta;
        this.numeroCivico = numeroCivico;
        this.CAP = CAP;
    }

//-----------------METODI SET---------------------------------------

    /**
     * Imposta il nome del cliente.
     *
     * @param nome Nome da impostare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome del cliente.
     *
     * @param cognome Cognome da impostare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta il numero telefonico del cliente.
     *
     * @param numeroTelefonico Numero telefonico da impostare
     */
    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    /**
     * Imposta il numero della carta di credito del cliente.
     *
     * @param numeroCartaDiCredito Numero carta di credito da impostare
     */
    public void setNumeroCartaDiCredito(String numeroCartaDiCredito) {
        this.numeroCartaDiCredito = numeroCartaDiCredito;
    }

    /**
     * Imposta il credito disponibile del cliente.
     *
     * @param credito Credito da impostare
     */
    public void setCredito(double credito) {
        this.credito = credito;
    }

    /**
     * Imposta la via dell'indirizzo del cliente.
     *
     * @param via Via da impostare
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Imposta la città dell'indirizzo del cliente.
     *
     * @param citta Città da impostare
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * Imposta il numero civico dell'indirizzo del cliente.
     *
     * @param numeroCivico Numero civico da impostare
     */
    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * Imposta il CAP (Codice di Avviamento Postale) dell'indirizzo.
     *
     * @param CAP CAP da impostare
     */
    public void setCAP(String CAP) {
        this.CAP = CAP;
    }

// ---------------- METODI GET ----------------------

    /**
     * Restituisce il nome del cliente.
     *
     * @return Nome del cliente
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce il cognome del cliente.
     *
     * @return Cognome del cliente
     */
    public String getCognome() {
        return this.cognome;
    }

    /**
     * Restituisce il numero telefonico del cliente.
     *
     * @return Numero telefonico del cliente
     */
    public String getNumeroTelefonico() {
        return this.numeroTelefonico;
    }

    /**
     * Restituisce il numero della carta di credito del cliente.
     *
     * @return Numero carta di credito
     */
    public String getNumeroCartaDiCredito() {
        return this.numeroCartaDiCredito;
    }

    /**
     * Restituisce il credito disponibile del cliente.
     *
     * @return Credito disponibile
     */
    public double getCredito() {
        return this.credito;
    }

    /**
     * Restituisce la via dell'indirizzo del cliente.
     *
     * @return Via dell'indirizzo
     */
    public String getVia() {
        return this.via;
    }

    /**
     * Restituisce la città dell'indirizzo del cliente.
     *
     * @return Città dell'indirizzo
     */
    public String getCitta() {
        return this.citta;
    }

    /**
     * Restituisce il numero civico dell'indirizzo del cliente.
     *
     * @return Numero civico
     */
    public String getNumeroCivico() {
        return this.numeroCivico;
    }

    /**
     * Restituisce il CAP dell'indirizzo del cliente.
     *
     * @return CAP (Codice di Avviamento Postale)
     */
    public String getCAP() {
        return this.CAP;
    }

    /**
     * Restituisce una rappresentazione testuale dell'indirizzo del cliente.
     *
     * @return Stringa contenente via, numero civico, CAP e città
     */
    @Override
    public String toString() {
        return this.via + " " + this.numeroCivico + ",  " + this.CAP + " " + this.citta;
    }
}
