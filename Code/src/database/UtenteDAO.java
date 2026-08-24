package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Utenti" del database.
 */

import Entity.EntityUtente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {

    private Connection conn;

    /**
     * Costruttore della classe UtenteDAO.
     * Stabilisce una connessione al database tramite DBManager.
     * 
     * @throws SQLException se la connessione al database fallisce
     */
    public UtenteDAO() throws SQLException {
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

//-------------------------------------------------------LOGIN------------------------------------------------------------

    /**
     * Legge un utente dal database tramite email (e opzionalmente password).
     * 
     * @param email email dell'utente da cercare
     * @return EntityUtente se l'utente esiste, null altrimenti
     * @throws SQLException in caso di errori SQL
     */
    public EntityUtente leggiUtente(String email) throws SQLException {

        EntityUtente utente = null;
        
        try {
            String query = "SELECT * FROM utenti WHERE email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                utente = new EntityUtente(result.getString("email"), result.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println("Connessione fallita! (leggi Utente)");
            e.printStackTrace();
        }

        return utente;

    }

//----------------------------------------------------REGISTRAZIONE---------------------------------------------------------

    /**
     * Inserisce un nuovo utente nella tabella utenti se l'email non esiste già.
     * 
     * @param email email del nuovo utente
     * @param password password del nuovo utente
     * @return true se inserimento avvenuto con successo, false se email già presente
     * @throws SQLException in caso di errori SQL
     */
    public boolean inserisciUtente(String email, String password) throws SQLException {
        
        boolean aggiunto = false;
        
        try{
            String query = "INSERT INTO utenti (email, password) VALUES (?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, password);

            stmt.executeUpdate();
            
            aggiunto = true;
        
        }catch (SQLException e){
            System.out.println("Connessione fallita! (inserisci Utente)");
            e.printStackTrace();
        }
        
        return aggiunto;
    }
    
    
    
}
