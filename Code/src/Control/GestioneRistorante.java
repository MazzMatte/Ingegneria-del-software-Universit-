package Control;

import database.MenuDAO;
import database.RistoratoreDAO;

import Entity.EntityMenuPizza;
import Entity.EntityRistoratore;
import Exception.CostoNonValidoException;
import Exception.PizzaGiaPresenteException;
import Exception.PizzaNonPresenteException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe che gestisce le operazioni eseguibili sul ristorante.
 * Fornisce metodi per aggiungere, modificare ed eliminare pizze,
 * nonché per recuperare menu e lista ristoranti filtrati per CAP.
 */
public class GestioneRistorante {

//---------------------------------------------MODIFICA MENU--------------------------------------------------------------------------     
    
    /**
     * Aggiunge una nuova pizza al menu del ristoratore loggato.
     * 
     * @param nuovaPizza Oggetto EntityMenuPizza contenente i dati della pizza da aggiungere
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     * @throws SQLException in caso di errore di connessione al database
     * @throws Exception.CostoNonValidoException In caso il costo inserito sia negativo
     * @throws Exception.PizzaGiaPresenteException In caso di pizza gia presente nel menu
    */
    public boolean aggiungiPizza(EntityMenuPizza nuovaPizza) throws SQLException, CostoNonValidoException, PizzaGiaPresenteException {
        
        boolean presente;
        boolean aggiunta;
        MenuDAO nuovaPizzaDAO = new MenuDAO();
        
        costoValido (nuovaPizza.getCosto());
        
        presente = nuovaPizzaDAO.pizzaPresente(nuovaPizza.getEmailRistoratore(), nuovaPizza.getNomePizza());
        if (presente == true){
            
            //Se la pizza è presente allora stop
            nuovaPizzaDAO.close();
            throw new PizzaGiaPresenteException();
            
        } 
        
        aggiunta = nuovaPizzaDAO.nouvaPizza(nuovaPizza);
        
        nuovaPizzaDAO.close();
        
        return aggiunta;
    }
    
    /**
     * Modifica una pizza esistente nel menu del ristoratore loggato.
     * 
     * @param emailRistoratore Email del ristoratore che possiede il menu
     * @param nomePizza Nome della pizza da modificare
     * @param nuovaDescrizione Nuova descrizione della pizza
     * @param nuovoCosto Nuovo costo della pizza
     * @return true se la modifica è stata effettuata con successo, false altrimenti
     * @throws SQLException in caso di errore di connessione al database
     * @throws Exception.CostoNonValidoException
     * @throws Exception.PizzaNonPresenteException se la pizza NON è presente
    */
    public boolean modificaPizza(String emailRistoratore, String nomePizza, String nuovaDescrizione, double nuovoCosto) throws SQLException, CostoNonValidoException, PizzaNonPresenteException {
        
        boolean presente;
        boolean modificata = false;
        
        MenuDAO modificaPizzaDAO = new MenuDAO();
        costoValido (nuovoCosto);
        presente = modificaPizzaDAO.pizzaPresente(emailRistoratore, nomePizza);
        if (presente){
             //Se la pizza è presente allora
            modificata = modificaPizzaDAO.modificaPizza(emailRistoratore, nomePizza, nuovaDescrizione, nuovoCosto);
        } else{
            throw new PizzaNonPresenteException();
        } 
        
        modificaPizzaDAO.close();
        
        return modificata;
    }
    
    /**
     * Elimina una pizza dal menu del ristoratore loggato.
     * 
     * @param emailRistoratore Email del ristoratore che possiede il menu
     * @param nomePizza Nome della pizza da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     * @throws SQLException in caso di errore di connessione al database
    */
    public boolean eliminaPizza(String emailRistoratore, String nomePizza) throws SQLException {
        
        boolean presente;
        boolean eliminata = false;
        
        MenuDAO eliminaPizzaDAO = new MenuDAO();
        
        presente = eliminaPizzaDAO.pizzaPresente(emailRistoratore, nomePizza);
        
        if (presente){
            //Se la pizza è presente allora
            eliminata = eliminaPizzaDAO.eliminaPizza(emailRistoratore, nomePizza);
        } 
        
        eliminaPizzaDAO.close();
        
        return eliminata;
    }
 
//-------------------------------------------VISUALIZZAZIONE RISTORANTI-----------------------------------------------------------------------        
    
    /**
     * Restituisce la lista dei ristoranti presenti in una determinata zona, filtrando per CAP.
     * 
     * @param CAP Codice di Avviamento Postale utilizzato come filtro per i ristoranti
     * @return ArrayList di EntityRistoratore contenente i ristoranti trovati
     * @throws SQLException in caso di errore di connessione al database
    */
    public ArrayList<EntityRistoratore> listaRistoranti(String CAP) throws SQLException {
        
        ArrayList<EntityRistoratore> listaRistoranti;
        RistoratoreDAO ristorantiDAO = new RistoratoreDAO();
        
        listaRistoranti = ristorantiDAO.ottieniRistoranti(CAP);
        
        ristorantiDAO.close();
        
        return listaRistoranti;
    }    
    
//---------------------------------------------VISUALIZZAZIONE MENU--------------------------------------------------------------------------        
    
    /**
     * Restituisce il menu completo di un ristorante identificato dall'email del ristoratore.
     * 
     * @param emailRistoratore Email del ristoratore proprietario del menu
     * @return ArrayList di EntityMenuPizza contenente tutte le pizze del menu
     * @throws SQLException in caso di errore di connessione al database
    */
    public ArrayList<EntityMenuPizza> menu(String emailRistoratore) throws SQLException {
        
        ArrayList<EntityMenuPizza> menu;
        MenuDAO menuDAO = new MenuDAO();
        
        menu = menuDAO.ottieniMenu(emailRistoratore);
        
        menuDAO.close();
        
        return menu;
    }
    
//------------------------VALIDAZIONE DEI DATI-------------------------------
    
    /**
     * Verfica che il costo della pizza sia un numero positivo.
     * 
     * @param costo Costo da verificare
     * @throws Exception.CostoNonValidoException In caso il costo inserito sia negativo
    */
    public void costoValido (double costo) throws CostoNonValidoException{
        
        if (costo <= 0){
            throw new CostoNonValidoException();
        }
        
    }
}
