package Entity;

/**
 * Rappresenta un'associazione tra un ordine e le pizze ordinate,
 * specificando la quantità di ciascuna pizza all'interno dell'ordine.
 */
public class EntityOrdinePizza {
    private int codiceUnivoco;  // Codice univoco dell'ordine a cui questa pizza appartiene
    private String nomePizza;   // Nome della pizza ordinata
    private int quantita;       // Quantità di questa pizza nell'ordine
    
    /**
     * Costruttore parametrizzato.
     * 
     * @param codiceUnivoco codice univoco dell'ordine
     * @param nomePizza nome della pizza ordinata
     * @param quantita quantità di pizze ordinate
     */
    public EntityOrdinePizza(int codiceUnivoco, String nomePizza, int quantita) {
        this.codiceUnivoco = codiceUnivoco;
        this.nomePizza = nomePizza;
        this.quantita = quantita;
    }
    
    /**
     * Restituisce il codice univoco dell'ordine.
     * 
     * @return codice univoco ordine
     */
    public int getCodiceUnivoco() {
        return this.codiceUnivoco;
    }
    
    /**
     * Restituisce il nome della pizza ordinata.
     * 
     * @return nome pizza
     */
    public String getNomePizza() {
        return this.nomePizza;
    }
    
    /**
     * Restituisce la quantità di questa pizza nell'ordine.
     * 
     * @return quantità ordinata
     */
    public int getQuantita() {
        return this.quantita;
    }
    
    /**
     * Imposta il codice univoco dell'ordine.
     * 
     * @param codiceUnivoco nuovo codice ordine
     */
    public void setCodiceUnivoco(int codiceUnivoco) {
        this.codiceUnivoco = codiceUnivoco;
    }
    
    /**
     * Imposta il nome della pizza.
     * 
     * @param nomePizza nuovo nome pizza
     */
    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }
    
    /**
     * Imposta la quantità di pizza ordinata.
     * 
     * @param quantita nuova quantità
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
