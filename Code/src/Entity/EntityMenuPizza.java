package Entity;

/**
 * Rappresenta una pizza presente nel menu di un ristorante.
 * L'identificativo univoco è la combinazione di emailRistoratore e nomePizza.
 */
public class EntityMenuPizza {
    private String emailRistoratore;
    private String nomePizza;
    private String descrizione;
    private double costo;

    /**
     * Costruttore parametrizzato per creare un oggetto EntityMenuPizza.
     * 
     * @param emailRistoratore Email identificativa del ristorante proprietario del menu
     * @param nomePizza Nome della pizza
     * @param descrizione Descrizione della pizza
     * @param costo Costo della pizza
     */
    public EntityMenuPizza(String emailRistoratore, String nomePizza, String descrizione, double costo) {
        this.emailRistoratore = emailRistoratore;
        this.nomePizza = nomePizza;
        this.descrizione = descrizione;
        this.costo = costo;
    }

// METODI GET ---------------------------------------------------------------------------------------------------------

    /**
     * Restituisce l'email del ristoratore a cui appartiene questa pizza.
     * 
     * @return email del ristoratore
     */
    public String getEmailRistoratore() {
        return this.emailRistoratore;
    }

    /**
     * Restituisce il nome della pizza.
     * 
     * @return nome della pizza
     */
    public String getNomePizza() {
        return this.nomePizza;
    }

    /**
     * Restituisce la descrizione della pizza.
     * 
     * @return descrizione della pizza
     */
    public String getDescrizione() {
        return this.descrizione;
    }

    /**
     * Restituisce il costo della pizza.
     * 
     * @return costo della pizza
     */
    public double getCosto() {
        return this.costo;
    }

// METODI SET ---------------------------------------------------------------------------------------------------------

    /**
     * Imposta l'email del ristoratore a cui appartiene questa pizza.
     * 
     * @param emailRistoratore nuova email del ristoratore
     */
    public void setEmailRistoratore(String emailRistoratore) {
        this.emailRistoratore = emailRistoratore;
    }

    /**
     * Imposta il nome della pizza.
     * 
     * @param nomePizza nuovo nome della pizza
     */
    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }

    /**
     * Imposta la descrizione della pizza.
     * 
     * @param descrizione nuova descrizione della pizza
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Imposta il costo della pizza.
     * 
     * @param costo nuovo costo della pizza
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

}
