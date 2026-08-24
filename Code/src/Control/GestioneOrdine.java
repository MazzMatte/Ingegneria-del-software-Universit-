package Control;

import Entity.EntityCliente;
import database.OrdineDAO;
import database.RiderDAO;
import database.ClienteDAO;
import database.MenuDAO;
import database.OrdinePizzaDAO;

import Entity.EntityOrdine;
import Entity.EntityOrdinePizza;
import Entity.EntityRistoratore;
import Exception.CreditoNonSufficienteException;
import Exception.QuantitaNonValidaException;
import database.RistoratoreDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che gestisce la logica relativa agli ordini nel sistema Pizza@Casa.
 * Include funzionalità per il rider (presa e conferma ordini), 
 * l'inserimento di un ordine da parte del cliente e la visualizzazione degli ordini 
 * per i ristoratori.
 */
public class GestioneOrdine {
    
//---------------------------------------ORDINI DEL RIDER---------------------------------------------------------------
    
    /**
     * Ottiene un ordine per un rider in base al CAP di lavoro e alla email del rider.
     * Imposta lo stato del rider come "occupato".
     * 
     * @param CAPDiLavoro CAP dell'area di lavoro del rider
     * @param emailRider Email identificativa del rider
     * @return EntityOrdine l'ordine assegnato al rider
     * @throws SQLException in caso di errore di accesso al database
     */
    public EntityOrdine ottieniOrdine(String CAPDiLavoro, String emailRider) throws SQLException {
       
        OrdineDAO nuovoOrdineDAO = new OrdineDAO();
        RiderDAO occupato = new RiderDAO();
        
        EntityOrdine ordine = nuovoOrdineDAO.ottieniOrdine(CAPDiLavoro, emailRider);
        occupato.setRiderOccupato(emailRider);
        
        nuovoOrdineDAO.close();
        occupato.close();
        
        return ordine;
    }

    /**
     * Il rider conferma la consegna di un ordine, aggiornando lo stato dell'ordine
     * e liberando il rider.
     * 
     * @param codiceOrdine Codice univoco dell'ordine da confermare
     * @param emailRider Email del rider che conferma la consegna
     * @return true se la conferma è andata a buon fine, false altrimenti
     * @throws SQLException in caso di errore di accesso al database
     */
    public boolean confermaConsegna(int codiceOrdine, String emailRider) throws SQLException {
        
        OrdineDAO confermaOrdine = new OrdineDAO();
        RiderDAO libero = new RiderDAO();
        
        boolean confermato = confermaOrdine.confermaConsegna(codiceOrdine);
        libero.setRiderLibero(emailRider);
        
        confermaOrdine.close();
        libero.close();
        
        return confermato;
    }

    /**
     * Il rider Ottiene i dati del ristorante a cui deve consegnare.
     * 
     * @param emailRistoratore E-mail del ristoratore dell'ordine
     * @return i dati del ristorante
     */
    public EntityRistoratore ottieniRistorante (String emailRistoratore)throws SQLException{
        RistoratoreDAO ristoranteDAO = new RistoratoreDAO();
        
        EntityRistoratore ristoratore = ristoranteDAO.ottieniRistoratore(emailRistoratore);
        
        ristoranteDAO.close();
        
        return ristoratore;
    }
    
    /**
     * Il rider Ottiene i dati del cliente a cui deve consegnare.
     * 
     * @param emailCliente E-mail del cliente dell'ordine
     * @return i dati del cliente
     */
    public EntityCliente ottieniCliente (String emailCliente)throws SQLException{
        ClienteDAO clienteDAO = new ClienteDAO();
        
        EntityCliente cliente = clienteDAO.ottieniCliente(emailCliente);
        
        clienteDAO.close();
        
        return cliente;
    }
    
    
//--------------------------------------INSERIMENTO ORDINE DA CLIENTE---------------------------------------------
    
    /**
     * Inserisce un nuovo ordine nel sistema, verificando la disponibilità economica del cliente,
     * calcolando il costo totale e salvando le pizze ordinate.
     * 
     * @param emailCliente Email del cliente che effettua l'ordine
     * @param emailRistoratore Email del ristoratore a cui è destinato l'ordine
     * @param carrello Lista di stringhe contenenti nomePizza e quantità, formattate come "nomePizza;quantità"
     * @return true se l'ordine è stato inserito correttamente, false altrimenti (es. credito insufficiente)
     * @throws SQLException in caso di errore di accesso al database
     * @throws Exception.CreditoNonSufficienteException Il cliente non ha abbastanza soldi sulla carta
     */
    public boolean inserisciOrdine(String emailCliente, String emailRistoratore, ArrayList<String> carrello) throws SQLException, CreditoNonSufficienteException {
        boolean inserito = false;
        double costoTOT = 0;
        double costoPizza;
        
        MenuDAO menuDAO = new MenuDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        
        // Calcolo costo totale
        for (String pizza : carrello) {
            String[] parti = pizza.split(";");
            String nomePizza = parti[0].trim();
            int quantita = Integer.parseInt(parti[1].trim());
            
            costoPizza = menuDAO.ottieniCosto(emailRistoratore, nomePizza);
            
            costoTOT += costoPizza * quantita;
        }

        // Verifico se il cliente può sostenere la spesa
        if (clienteDAO.verificaDisponibilita(emailCliente, costoTOT)) {
            inserito = true;
            LocalDate dataOrdine = LocalDate.now();
            LocalTime oraProntaConsegna = LocalTime.now();
            
            int codiceUnivoco;
            
            // Creo l'ordine e ottengo il codice univoco auto-incrementale
            OrdineDAO ordineDAO = new OrdineDAO();
            codiceUnivoco = ordineDAO.aggiungiOrdine(emailCliente, emailRistoratore, costoTOT, dataOrdine, oraProntaConsegna);
            
            // Detraggo l'importo dal credito residuo del cliente
            clienteDAO.detraiSpesa(emailCliente, costoTOT);
            
            OrdinePizzaDAO ordinePizzaDao = new OrdinePizzaDAO();
            
            // Salvo pizze e quantità nell'ordine
            for (String pizza : carrello) {
                String[] parti = pizza.split(";");
                String nomePizza = parti[0].trim();
                int quantita = Integer.parseInt(parti[1].trim());
                ordinePizzaDao.aggiungiOrdinePizza(codiceUnivoco, nomePizza, quantita);
            }
            
            ordinePizzaDao.close();
            ordineDAO.close();
        } else{
            throw new CreditoNonSufficienteException();
        } 
        
        clienteDAO.close();
        menuDAO.close();
        
        return inserito;
    }

//-----------------------------------------VISUALIZZAZIONE DEGLI ORDINI--------------------------------------------------------------------------        
    /**
     * Recupera tutte le pizze ordinate per gli ordini di un dato ristoratore.
     * 
     * @param emailRistoratore Email del ristoratore di cui visualizzare gli ordini
     * @return ArrayList di EntityOrdinePizza contenente le pizze ordinate
     * @throws SQLException in caso di errore di accesso al database
     */
    public ArrayList<EntityOrdinePizza> ordini(String emailRistoratore) throws SQLException {
        
        // Ottengo gli ordini per il ristoratore
        OrdineDAO ottieniOrdiniDAO = new OrdineDAO(); 
        ArrayList<EntityOrdine> codiciOrdini = ottieniOrdiniDAO.ottieniOrdini(emailRistoratore);
        
        // Ottengo le pizze relative agli ordini
        OrdinePizzaDAO ottieniOrdinePizzeDAO = new OrdinePizzaDAO();
        ArrayList<EntityOrdinePizza> pizzeOrdine = ottieniOrdinePizzeDAO.ottieniOrdinePizze(codiciOrdini);
        
        ottieniOrdiniDAO.close();
        ottieniOrdinePizzeDAO.close();
        
        return pizzeOrdine;
    }



//-------------------------------------------REPORT------------------------------------------------------

    /**
     * Ottiene il report per mese e anno.
     * @param mese il mese del report (1-12)
     * @param anno l'anno del report
     * @return lista di array Object[] contenente CAP, durata (Duration) e numero totale ordini (Integer)
     * @throws SQLException
     */
    public List<Object[]> ottieniReportPerMeseAnno(int mese, int anno) throws SQLException {

        OrdineDAO ordineDAO = new OrdineDAO();

        List<Object[]> report; 

        report = ordineDAO.getReportPerMeseAnno(mese, anno);

        ordineDAO.close();

        return report;
    }


//------------------------VALIDAZIONE DEI DATI-------------------------------
    
    /**
     * Verfica che La quantità della pizza sia un numero positivo.
     * 
     * @param quantita Numero da verificare
     * @throws Exception.QuantitaNonValidaException In caso il costo inserito sia negativo
    */
    public void quantitaValido (int quantita) throws QuantitaNonValidaException{
        
        if (quantita <= 0){
            throw new QuantitaNonValidaException();
        }
        
    }
}
