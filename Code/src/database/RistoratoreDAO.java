package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Ristoratori" del database.
 */

import Entity.EntityRistoratore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RistoratoreDAO {
    
    private Connection conn;
    
    /**
     * Costruttore della classe RistoratoreDAO.
     * Stabilisce una connessione al database tramite DBManager.
     * 
     * @throws SQLException se la connessione al database fallisce
     */
    public RistoratoreDAO() throws SQLException {
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
     * Ottiene i dati del ristoratore che ha effettuato l'accesso (login).
     * 
     * @param email email del ristoratore
     * @param password password del ristoratore (non verificata in questo metodo)
     * @return EntityRistoratore con i dati del ristoratore se trovato, altrimenti null
     * @throws SQLException se si verifica un errore nel database
     */
    public EntityRistoratore leggiRistoratore(String email, String password) throws SQLException {
        
        EntityRistoratore ristoratore = null;
        
        try {
            
            String query = "SELECT * FROM ristoratori WHERE Email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                ristoratore = new EntityRistoratore(
                    email,
                    password,
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7),
                    result.getString(8),
                    result.getString(9)
                );	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
        
        return ristoratore;
    }
    
    /**
     * Ottiene i dati del ristoratore tramite email (senza password).
     * 
     * @param email email del ristoratore da cercare
     * @return EntityRistoratore con i dati del ristoratore se trovato, altrimenti null
     * @throws SQLException se si verifica un errore nel database
     */
    public EntityRistoratore ottieniRistoratore(String email) throws SQLException {
        
        EntityRistoratore ristoratore = null;
        
        try {
            String query = "SELECT * FROM ristoratori WHERE Email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                ristoratore = new EntityRistoratore(
                    email,
                    null,
                    result.getString("nome"),
                    result.getString("cognome"),
                    result.getString("nomeEsercizioCommerciale"),
                    result.getString("recapitoTelefonico"),
                    result.getString("via"),
                    result.getString("citta"),
                    result.getString("numeroCivico"),
                    result.getString("CAP")
                );	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (ottieni ristoratore)");
            e.printStackTrace();
        }
        
        return ristoratore;
    }
 
    
    
//-----------------------------------------------------REGISTRAZIONE------------------------------------------------------------

    /**
     * Inserisce un nuovo Ristoratore nel database.
     *
     * @param nuovoRistoratore nuovo Record del database
     * @return
     * @throws SQLException se si verifica un errore nel database
     */
    public boolean aggiungiRistoratore(EntityRistoratore nuovoRistoratore) throws SQLException{
        
        try{
            String query = "INSERT INTO ristoratori (email, nome, cognome, nomeEsercizioCommerciale, recapitoTelefonico, via, citta, numeroCivico, CAP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuovoRistoratore.getEmail());
            stmt.setString(2, nuovoRistoratore.getNome());
            stmt.setString(3, nuovoRistoratore.getCognome());
            stmt.setString(4, nuovoRistoratore.getNomeEsercizioCommerciale());
            stmt.setString(5, nuovoRistoratore.getRecapitoTelefonico());
            stmt.setString(6, nuovoRistoratore.getVia());
            stmt.setString(7, nuovoRistoratore.getCitta());
            stmt.setString(8, nuovoRistoratore.getNumeroCivico());
            stmt.setString(9, nuovoRistoratore.getCAP());

            if(stmt.executeUpdate() > 0){
                return true;	
            }
        }catch(SQLException e){
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Ottiene la lista di ristoranti che si trovano in una determinata area CAP.
     * 
     * @param CAP CAP di riferimento per filtrare i ristoranti
     * @return ArrayList di EntityRistoratore corrispondenti alla zona CAP
     * @throws SQLException se si verifica un errore nel database
     */
    public ArrayList<EntityRistoratore> ottieniRistoranti(String CAP) throws SQLException {
        
        ArrayList<EntityRistoratore> ristoranti = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM ristoratori WHERE CAP = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, CAP);
            ResultSet rs = stmt.executeQuery();
            
            // Costruisco la lista di ristoranti in zona (per CAP)
            while (rs.next()) {
                String email = rs.getString("email");
                String nomeEsercizioCommerciale = rs.getString("nomeEsercizioCommerciale");
                String recapitoTelefonico = rs.getString("recapitoTelefonico");
                String via = rs.getString("via");
                String citta = rs.getString("citta");
                String numeroCivico = rs.getString("numeroCivico");
                
                ristoranti.add(new EntityRistoratore(email, null, null, null, nomeEsercizioCommerciale, recapitoTelefonico, via, citta, numeroCivico, CAP));
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita! (Ottieni Ristoranti)");
            e.printStackTrace();
        }
                
        return ristoranti;
    }
}
