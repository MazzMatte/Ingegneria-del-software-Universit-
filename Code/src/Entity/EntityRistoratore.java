package Entity;

/**
 * Classe che rappresenta un Ristoratore, estende EntityUtente.
 * Contiene informazioni personali e dati relativi all'esercizio commerciale.
 */
public class EntityRistoratore extends EntityUtente {
    private String nome;
    private String cognome;
    private String nomeEsercizioCommerciale;
    private String recapitoTelefonico;
    private String via;
    private String citta;
    private String numeroCivico;
    private String CAP;

    /**
     * Costruttore con parametri per inizializzare un ristoratore.
     *
     * @param email Email del ristoratore (identificativo unico).
     * @param password Password associata all'account.
     * @param nome Nome del ristoratore.
     * @param cognome Cognome del ristoratore.
     * @param nomeEsercizioCommerciale Nome dell'esercizio commerciale.
     * @param recapitoTelefonico Numero di telefono di contatto.
     * @param via Via dell'esercizio commerciale.
     * @param citta Città dell'esercizio commerciale.
     * @param numeroCivico Numero civico dell'esercizio commerciale.
     * @param CAP CAP dell'esercizio commerciale.
     */
    public EntityRistoratore(String email, String password, String nome, String cognome, String nomeEsercizioCommerciale,
                            String recapitoTelefonico, String via, String citta, String numeroCivico, String CAP) {
        super(email, password);
        this.nome = nome;
        this.cognome = cognome;
        this.nomeEsercizioCommerciale = nomeEsercizioCommerciale;
        this.recapitoTelefonico = recapitoTelefonico;
        this.via = via;
        this.citta = citta;
        this.numeroCivico = numeroCivico;
        this.CAP = CAP;
    }

    /**
     * Imposta il nome del ristoratore.
     * @param nome Nome del ristoratore.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome del ristoratore.
     * @param cognome Cognome del ristoratore.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta il nome dell'esercizio commerciale.
     * @param nomeEsercizioCommerciale Nome dell'esercizio commerciale.
     */
    public void setNomeEsercizioCommerciale(String nomeEsercizioCommerciale) {
        this.nomeEsercizioCommerciale = nomeEsercizioCommerciale;
    }

    /**
     * Imposta il recapito telefonico.
     * @param recapitoTelefonico Numero di telefono di contatto.
     */
    public void setRecapitoTelefonico(String recapitoTelefonico) {
        this.recapitoTelefonico = recapitoTelefonico;
    }

    /**
     * Imposta la via dell'esercizio commerciale.
     * @param via Via dell'esercizio commerciale.
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Imposta la città dell'esercizio commerciale.
     * @param citta Città dell'esercizio commerciale.
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * Imposta il numero civico dell'esercizio commerciale.
     * @param numeroCivico Numero civico.
     */
    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * Imposta il CAP dell'esercizio commerciale.
     * @param CAP Codice Postale.
     */
    public void setCAP(String CAP) {
        this.CAP = CAP;
    }

    /**
     * Restituisce il nome del ristoratore.
     * @return Nome.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce il cognome del ristoratore.
     * @return Cognome.
     */
    public String getCognome() {
        return this.cognome;
    }

    /**
     * Restituisce il nome dell'esercizio commerciale.
     * @return Nome dell'esercizio commerciale.
     */
    public String getNomeEsercizioCommerciale() {
        return this.nomeEsercizioCommerciale;
    }

    /**
     * Restituisce il recapito telefonico.
     * @return Numero di telefono.
     */
    public String getRecapitoTelefonico() {
        return this.recapitoTelefonico;
    }

    /**
     * Restituisce la via dell'esercizio commerciale.
     * @return Via.
     */
    public String getVia() {
        return this.via;
    }

    /**
     * Restituisce la città dell'esercizio commerciale.
     * @return Città.
     */
    public String getCitta() {
        return this.citta;
    }

    /**
     * Restituisce il numero civico.
     * @return Numero civico.
     */
    public String getNumeroCivico() {
        return this.numeroCivico;
    }

    /**
     * Restituisce il CAP.
     * @return Codice Postale.
     */
    public String getCAP() {
        return this.CAP;
    }

    /**
     * Restituisce una rappresentazione testuale dell'indirizzo completo.
     * @return Indirizzo completo in formato stringa.
     */
    @Override
    public String toString() {
        return (this.via + " " + this.numeroCivico + ", " + this.CAP + " " + this.citta);
    }
}
