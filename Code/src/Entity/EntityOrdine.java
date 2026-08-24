package Entity;

import java.sql.Time;  // Per ottenere l'orario da SALVARE sul DB
import java.sql.Date;  // Per ottenere la data da SALVARE nel DB

/**
 * Rappresenta un ordine effettuato nel sistema.
 * Contiene informazioni su orari di consegna, stato dell'ordine, e le email dei partecipanti (cliente, ristoratore, rider).
 */
public class EntityOrdine {
    private int codiceUnivoco;  // Inizializzato automaticamente nel DB (AUTO_INCREMENT)
    private Time oraProntaConsegna;
    private Time oraFineConsegna;
    private double costoTotale;
    private Date dataOrdine;
    private String stato;       // "in attesa", "pronta consegna", "consegnato"
    private String emailCliente;
    private String emailRistoratore;
    private String emailRider;  // può essere null se non assegnato
    
    /**
     * Costruttore parametrizzato per creare un ordine completo.
     * 
     * @param codiceUnivoco codice univoco dell'ordine (assegnato dal DB)
     * @param oraProntaConsegna orario in cui l'ordine è pronto per la consegna
     * @param oraFineConsegna orario in cui la consegna è terminata
     * @param costoTotale costo totale dell'ordine
     * @param dataOrdine data in cui è stato effettuato l'ordine
     * @param stato stato corrente dell'ordine ("in attesa", "pronta consegna", "consegnato")
     * @param emailCliente email del cliente che ha effettuato l'ordine
     * @param emailRistoratore email del ristoratore che ha ricevuto l'ordine
     * @param emailRider email del rider incaricato della consegna (può essere null)
     */
    public EntityOrdine(int codiceUnivoco, Time oraProntaConsegna, Time oraFineConsegna, double costoTotale, Date dataOrdine, String stato, String emailCliente, String emailRistoratore, String emailRider) {
        this.codiceUnivoco = codiceUnivoco;
        this.oraProntaConsegna = oraProntaConsegna;
        this.oraFineConsegna = oraFineConsegna;
        this.costoTotale = costoTotale;
        this.dataOrdine = dataOrdine;
        this.stato = stato;
        this.emailCliente = emailCliente;
        this.emailRistoratore = emailRistoratore;
        this.emailRider = emailRider;
    }

    /**
     * Costruttore vuoto.
     */
    public EntityOrdine() {
    }
    
// METODI GET --------------------------------------------------------------
    
    /**
     * Restituisce il codice univoco dell'ordine.
     * 
     * @return codice univoco
     */
    public int getCodiceUnivoco() {
        return this.codiceUnivoco;
    }

    /**
     * Restituisce l'orario in cui l'ordine è pronto per la consegna.
     * 
     * @return orario pronto consegna
     */
    public Time getOraProntaConsegna() {
        return this.oraProntaConsegna;
    }

    /**
     * Restituisce l'orario in cui la consegna è terminata.
     * 
     * @return orario fine consegna
     */
    public Time getOraFineConsegna() {
        return this.oraFineConsegna;
    }

    /**
     * Restituisce il costo totale dell'ordine.
     * 
     * @return costo totale
     */
    public double getCostoTotale() {
        return this.costoTotale;
    }

    /**
     * Restituisce la data in cui è stato effettuato l'ordine.
     * 
     * @return data dell'ordine
     */
    public Date getDataOrdine() {
        return this.dataOrdine;
    }

    /**
     * Restituisce lo stato corrente dell'ordine.
     * 
     * @return stato ("in attesa", "pronta consegna", "consegnato")
     */
    public String getStato() {
        return this.stato;
    }

    /**
     * Restituisce l'email del cliente che ha effettuato l'ordine.
     * 
     * @return email cliente
     */
    public String getEmailCliente() {
        return emailCliente;
    }

    /**
     * Restituisce l'email del ristoratore associato all'ordine.
     * 
     * @return email ristoratore
     */
    public String getEmailRistoratore() {
        return emailRistoratore;
    }

    /**
     * Restituisce l'email del rider incaricato della consegna.
     * 
     * @return email rider (può essere null se non assegnato)
     */
    public String getEmailRider() {
        return emailRider;
    }
    
// METODI SET --------------------------------------------------------------
    
    /**
     * Imposta il codice univoco dell'ordine.
     * 
     * @param codiceUnivoco nuovo codice univoco
     */
    public void setCodiceUnivoco(int codiceUnivoco) {
        this.codiceUnivoco = codiceUnivoco;
    }

    /**
     * Imposta l'orario in cui l'ordine è pronto per la consegna.
     * 
     * @param oraProntaConsegna nuovo orario pronto consegna
     */
    public void setOraProntaConsegna(Time oraProntaConsegna) {
        this.oraProntaConsegna = oraProntaConsegna;
    }

    /**
     * Imposta l'orario in cui la consegna è terminata.
     * 
     * @param oraFineConsegna nuovo orario fine consegna
     */
    public void setOraFineConsegna(Time oraFineConsegna) {
        this.oraFineConsegna = oraFineConsegna;
    }

    /**
     * Imposta il costo totale dell'ordine.
     * 
     * @param costoTotale nuovo costo totale
     */
    public void setCostoTotale(double costoTotale) {
        this.costoTotale = costoTotale;
    }

    /**
     * Imposta la data dell'ordine.
     * 
     * @param dataOrdine nuova data dell'ordine
     */
    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    /**
     * Imposta lo stato corrente dell'ordine.
     * 
     * @param stato nuovo stato ("in attesa", "pronta consegna", "consegnato")
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Imposta l'email del cliente associato all'ordine.
     * 
     * @param emailCliente nuova email cliente
     */
    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    /**
     * Imposta l'email del ristoratore associato all'ordine.
     * 
     * @param emailRistoratore nuova email ristoratore
     */
    public void setEmailRistoratore(String emailRistoratore) {
        this.emailRistoratore = emailRistoratore;
    }

    /**
     * Imposta l'email del rider incaricato della consegna.
     * 
     * @param emailRider nuova email rider (può essere null)
     */
    public void setEmailRider(String emailRider) {
        this.emailRider = emailRider;
    }
}
