package database;

/**
 * Classe DAO che agisce sulla tabella fisica "menuPizza" del database.
 */

import Entity.EntityMenuPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuDAO {
    
    private Connection conn;
    
    /**
     * Costruttore di MenuDAO.
     * Stabilisce la connessione al database tramite DBManager.
     * 
     * @throws SQLException in caso di errore di connessione al database
     */
    public MenuDAO() throws SQLException {
        conn = DBManager.getConnection();
    }
    
    /**
    * Chiude la connessione al database utilizzando il metodo statico {@link DBManager#closeConnection()}.
    *
    * @throws SQLException se si verifica un errore durante la chiusura della connessione
    */
   public void close() throws SQLException {
       conn = DBManager.closeConnection();
   }
    
    
//---------------------------------------------MODIFICHE AL MENU----------------------------------------------------
    
    /**
    * Verifica se una pizza è presente nel menu di un ristoratore specifico.
    *
    * @param emailRistoratore l'email identificativa del ristoratore
    * @param nomePizza il nome della pizza da cercare nel menu
    * @return true se la pizza è presente nel menu del ristoratore, false altrimenti
    * @throws SQLException se si verifica un errore durante l'esecuzione della query sul database
    */
    public boolean pizzaPresente (String emailRistoratore, String nomePizza)throws SQLException {
        
        boolean presente = false;
        
        try {
            String query = "SELECT * FROM menuPizza WHERE emailRistoratore = ? AND nomePizza = ?;" ;        
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, emailRistoratore);
            stmt.setString(2, nomePizza);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                presente = true;
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (Pizza Presente)");
            e.printStackTrace();
	} 
        
        return presente;
    }
    
    
    /**
     * Aggiunge una nuova pizza al menu di un ristoratore.
     * 
     * @param nuovaPizza oggetto EntityMenuPizza che rappresenta la pizza da aggiungere
     * @return true se la pizza è stata aggiunta con successo, false altrimenti
     * @throws SQLException in caso di errore durante l'accesso o la modifica del database
     */
    public boolean nouvaPizza(EntityMenuPizza nuovaPizza) throws SQLException {
        boolean aggiunto = false;
        
        try{    
            String query = "INSERT INTO menupizza (emailRistoratore, nomePizza, descrizionePizza, costo) VALUES (?, ?, ?, ?);" ;

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, nuovaPizza.getEmailRistoratore());
            stmt.setString(2, nuovaPizza.getNomePizza());
            stmt.setString(3, nuovaPizza.getDescrizione());
            stmt.setDouble(4, nuovaPizza.getCosto());

            if(stmt.executeUpdate() > 0) {
                aggiunto = true;	
            }
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (inserimento Pizza)");
            e.printStackTrace();
	}
        
        return aggiunto;
    }
    
    
    /**
     * Elimina una pizza dal menu di un ristoratore.
     * 
     * @param emailRistoratore email del ristoratore proprietario del menu
     * @param nomePizza nome della pizza da eliminare
     * @return true se la pizza è stata eliminata con successo, false altrimenti
     * @throws SQLException in caso di errore durante l'accesso o la modifica del database
     */
    public boolean eliminaPizza(String emailRistoratore,String nomePizza) throws SQLException {
        
        boolean eliminato = false;
        
        try {
            String query = "DELETE FROM menupizza WHERE emailRistoratore = ? AND nomePizza = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, emailRistoratore);
            stmt.setString(2, nomePizza);

            if(stmt.executeUpdate() > 0) {
                eliminato = true;	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (eliminazione Pizza)");
            e.printStackTrace();
	}
        
        return eliminato;
    }

    
    /**
     * Modifica la descrizione e il costo di una pizza già presente nel menu di un ristoratore.
     * 
     * @param emailRistoratore email del ristoratore proprietario del menu
     * @param nomePizza nome della pizza da modificare
     * @param nuovaDescrizione nuova descrizione da assegnare alla pizza
     * @param nuovoCosto nuovo costo da assegnare alla pizza
     * @return true se la pizza è stata modificata con successo, false altrimenti
     * @throws SQLException in caso di errore durante l'accesso o la modifica del database
     */
    public boolean modificaPizza (String emailRistoratore,String nomePizza,  String nuovaDescrizione, double nuovoCosto) throws SQLException {
        
        boolean modifica = false;
        
        try {
            String query = "UPDATE menupizza SET descrizionePizza = ?, costo = ? WHERE emailRistoratore = ? AND nomePizza = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuovaDescrizione);
            stmt.setDouble(2, nuovoCosto);
            stmt.setString(3, emailRistoratore);
            stmt.setString(4, nomePizza);

            if(stmt.executeUpdate() > 0) {
                modifica = true;	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (modifica Pizza)");
            e.printStackTrace();
	}
        
        return modifica;
    }
    
    
//---------------------------------------------STAMPA DEL MENU----------------------------------------------------
    
    
    /**
     * Ottiene il menu (lista di pizze) di un ristoratore.
     * 
     * @param emailRistoratore email del ristoratore di cui si vuole ottenere il menu
     * @return ArrayList di EntityMenuPizza contenente tutte le pizze del menu
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    public ArrayList<EntityMenuPizza> ottieniMenu (String emailRistoratore) throws SQLException {
        
        ArrayList<EntityMenuPizza> menu = new ArrayList<>();
        
        String nomePizza, descrizionePizza;
        double costo;
        
        try {
            String query = "SELECT * FROM menuPizza WHERE emailRistoratore = ? ;" ;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, emailRistoratore);
            ResultSet rs = stmt.executeQuery();
            
            //Costruisco il menu di emailRistoratore
            while (rs.next()) {
                nomePizza = rs.getString("nomePizza");
                descrizionePizza = rs.getString("descrizionePizza");
                costo = rs.getDouble ("costo");
                menu.add(new EntityMenuPizza(emailRistoratore, nomePizza, descrizionePizza, costo));
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (ottieniMenu)");
            e.printStackTrace();
	}
                
        return menu;
    }
    
    
//------------------------------------------OTTENIMENTO COSTO-----------------------------------------------------
    
    /**
     * Ottiene il costo di una specifica pizza di un ristoratore.
     * 
     * @param emailRistoratore email del ristoratore proprietario della pizza
     * @param nomePizza nome della pizza di cui si vuole conoscere il costo
     * @return costo della pizza, oppure -1 se non trovata o errore
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    public double ottieniCosto (String emailRistoratore, String nomePizza) throws SQLException {
        
        double costo = -1;
        
        try {
            String query = "SELECT * FROM menuPizza WHERE emailRistoratore = ? AND nomePizza = ?;" ;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, emailRistoratore);
            stmt.setString(2, nomePizza);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                costo = rs.getDouble("costo");
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (ottieniCosto in MenuDAO)");
            e.printStackTrace();
	} 
        
        return costo;
    }
}
