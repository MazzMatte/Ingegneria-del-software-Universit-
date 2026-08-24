package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Rider" del database.
 */

import Entity.EntityRider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiderDAO {
    
    private Connection conn;
    
    /**
     * Costruttore della classe RiderDAO.
     * Stabilisce una connessione al database tramite DBManager.
     * 
     * @throws SQLException se la connessione al database fallisce
     */
    public RiderDAO() throws SQLException{
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
    
//-----------------------------------------------------LOGIN-------------------------------------------------------------------------------   
    
    /**
     * Legge i dati del rider corrispondenti all'email fornita.
     * Nota: la password non viene verificata in questo metodo.
     * 
     * @param email email del rider da cercare
     * @param password password (non usata per la verifica, ma mantenuta per creare l'EntityRider)
     * @return EntityRider con i dati del rider se esiste, altrimenti null
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public static EntityRider leggiRider(String email, String password) throws SQLException {
        
        EntityRider rider = null;
        try{
            Connection conn = DBManager.getConnection();
            
            String query = "SELECT * FROM rider WHERE email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                rider = new EntityRider(email, password, result.getString("nome"), result.getString("cognome"),
                                        result.getString("CAPDiLavoro"), result.getString("stato"));	
            }
            
        } catch(SQLException e){
            System.out.println("Connessione fallita! (Leggi Rider)");
            e.printStackTrace();
        }
        
        return rider;
    }
    
//----------------------------------------------------ORDINE-------------------------------------------------------------------------------   
    
    /**
     * Imposta lo stato del rider specificato come 'occupato'.
     * 
     * @param emailRider email del rider da aggiornare
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public void setRiderOccupato(String emailRider) throws SQLException {
        try{
            String updateRider = "UPDATE rider SET stato = 'occupato' WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(updateRider);
            stmt.setString(1, emailRider);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Connessione fallita! (set rider occupato)");
            e.printStackTrace();
        }
    
    }

    /**
     * Imposta lo stato del rider specificato come 'libero'.
     * 
     * @param emailRider email del rider da aggiornare
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public void setRiderLibero(String emailRider) throws SQLException {
        
        try{
            String updateRider = "UPDATE rider SET stato = 'libero' WHERE email = ?";
            
            PreparedStatement stmt = conn.prepareStatement(updateRider);
            stmt.setString(1, emailRider);
            
            stmt.executeUpdate();
            
        }catch (SQLException e) {
            System.out.println("Connessione fallita! (leggi Utente)");
            e.printStackTrace();
        }
    
    }

//--------------------------------------------------REGISTRAZIONE-------------------------------------------------------------------------------   
    
    /**
     * Aggiunge un nuovo rider al database.
     * 
     * @param nuovoRider dati del nuovo Rider 
     * @return true se l'inserimento ha successo, false altrimenti
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public boolean aggiungiRider(EntityRider nuovoRider) throws SQLException {
       
        try{
            String query = "INSERT INTO rider (email, CAPDiLavoro, nome, cognome, stato) VALUES (?, ? , ?, ?, ?);";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuovoRider.getEmail());
            stmt.setString(2, nuovoRider.getCAPDiLavoro());
            stmt.setString(3, nuovoRider.getNome());
            stmt.setString(4, nuovoRider.getCognome());
            stmt.setString(5, nuovoRider.getStato());

            if (stmt.executeUpdate() > 0){
                return true;	
            }
            
        }catch(SQLException e){
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
        
        return false;
    }
    
}
