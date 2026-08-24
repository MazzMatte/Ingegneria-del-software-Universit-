package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Gestori" del database.
 */

import Entity.EntityGestore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestoreDAO {
    
    private Connection conn;
    
    /**
     * Costruttore che apre una connessione al database.
     * 
     * @throws SQLException se la connessione al database fallisce.
     */
    public GestoreDAO() throws SQLException {
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
    
    //--------------------------------------------------LOGIN--------------------------------------------------------
    
    /**
     * Restituisce i dati dell'entity Gestore corrispondente all'email e password fornite.
     * 
     * @param email email del gestore che tenta l'accesso.
     * @param password password del gestore che tenta l'accesso.
     * @return EntityGestore se l'email è trovata, altrimenti null.
     * @throws SQLException se c'è un errore nella query al database.
     */
    public static EntityGestore leggiGestore(String email, String password) throws SQLException {
        EntityGestore gestore = null;
        
        try {
            Connection conn = DBManager.getConnection();
            
            String query = "SELECT * FROM gestori WHERE Email = ? ;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                gestore = new EntityGestore(email, password);	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
        
        return gestore;
    }
}
