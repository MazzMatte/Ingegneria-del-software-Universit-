package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Clienti" del database.
 */

import Entity.EntityCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {
    
    private  Connection conn;
    
    /**
     * Costruttore che inizializza la connessione al database tramite DBManager.
     * 
     * @throws SQLException se la connessione al database fallisce
     */
    public ClienteDAO() throws SQLException{
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
     * Recupera i dati di un cliente dal database dato l'email e la password.
     * Utilizzato durante il login.
     * 
     * @param email Email del cliente da cercare.
     * @param password Password del cliente (non usata per query, ma mantenuta per creazione oggetto).
     * @return EntityCliente con i dati del cliente se trovato, null altrimenti.
     * @throws SQLException se si verifica un errore nella query SQL.
     */
    public EntityCliente leggiCliente (String email, String password) throws SQLException{
        
        EntityCliente cliente = null;
        
        try{
            String query = "SELECT * FROM clienti WHERE email = ? ;";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                cliente = new EntityCliente( email, password, result.getString(2), result.getString(3), result.getString(4), result.getString(9),  result.getDouble(10),result.getString(5),result.getString(6), result.getString(7), result.getString(8));	
            }
            
        }catch(SQLException e){
            System.out.println("Connessione fallita!");
            e.printStackTrace();
	}
        
        return cliente;
        
    }

    /**
     * Ottiene i dati del ristoratore tramite email (senza password).
     * 
     * @param email email del ristoratore da cercare
     * @return EntityRistoratore con i dati del ristoratore se trovato, altrimenti null
     * @throws SQLException se si verifica un errore nel database
     */
    public EntityCliente ottieniCliente(String email) throws SQLException {
        
        EntityCliente cliente = null;
        
        try {
            
            String query = "SELECT * FROM clienti WHERE Email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                cliente = new EntityCliente(
                    email,
                    null,
                    result.getString("nome"),
                    result.getString("cognome"),
                    result.getString("numeroTelefonico"),
                    result.getString("numeroCarta"),
                    result.getDouble("creditoResiduo"),
                    result.getString("via"),
                    result.getString("citta"),
                    result.getString("numeroCivico"),
                    result.getString("CAP")
                );	
            }
            
        } catch(SQLException e) {
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
        
        return cliente;
    }
    
//------------------------------------------------ REGISTRAZIONE--------------------------------------------------
    
    /**
     * Inserisce un nuovo cliente nel database.
     *
     * @param nuovoCliente nuovo Record del database
     * @throws SQLException
     */
    public void aggiungiCliente(EntityCliente nuovoCliente) throws SQLException{
       
        try{
            String query = "INSERT INTO clienti (email, nome, cognome, numeroTelefonico, via, citta, numeroCivico, CAP, numeroCarta, creditoResiduo) VALUES (?, ? , ?, ?, ?, ?, ?, ?, ?,?);" ;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuovoCliente.getEmail());
            stmt.setString(2, nuovoCliente.getNome());
            stmt.setString(3, nuovoCliente.getCognome());
            stmt.setString(4, nuovoCliente.getNumeroTelefonico());
            stmt.setString(5, nuovoCliente.getVia());
            stmt.setString(6, nuovoCliente.getCitta());
            stmt.setString(7, nuovoCliente.getNumeroCivico());
            stmt.setString(8, nuovoCliente.getCAP());
            stmt.setString(9, nuovoCliente.getNumeroCartaDiCredito());
            stmt.setDouble(10, nuovoCliente.getCredito());

            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Connessione fallita!");
            e.printStackTrace();
        }
    }
    
    //----------------------------------------------ORDINE------------------------------------------------------------
    
    /**
     * Verifica se il cliente ha credito residuo sufficiente per coprire il costo di un ordine.
     * 
     * @param email Email del cliente.
     * @param costo Costo totale dell'ordine.
     * @return true se il credito residuo è maggiore o uguale al costo, false altrimenti.
     * @throws SQLException se si verifica un errore nella query SQL.
     */
    public boolean verificaDisponibilita (String email, double costo)throws SQLException {
        
        boolean possibile = false;
        double creditoResiduo;
        
        try{
            // Ottengo il credito disponibile sulla carta del cliente
            String query = "SELECT * FROM clienti WHERE email = ? ;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            ResultSet result = stmt.executeQuery();
            
            if(result.next()){
                creditoResiduo = result.getDouble("creditoResiduo");
                possibile = creditoResiduo >= costo;
            }
            
            
        }catch(SQLException e){
            System.out.println("Connessione fallita ! (verificaDisponibilita in ClienteDAO)");
            e.printStackTrace();
        }
        
        return possibile;
    }
    
    /**
     * Detrarre il costo di un ordine dal credito residuo del cliente.
     * 
     * @param email Email del cliente.
     * @param costo Costo da detrarre.
     * @throws SQLException se si verifica un errore nella query SQL.
     */
    public void detraiSpesa (String email, double costo)throws SQLException {
        
        try{
            // Dettraggo il credito disponibile sulla carta del cliente
            String query = "UPDATE clienti SET creditoResiduo = (creditoResiduo - ?) WHERE email = ? ;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, costo);
            stmt.setString(2, email);
            
            stmt.executeUpdate();
            
        }catch(SQLException e){
            System.out.println("Connessione fallita! (detrai spesa)");
            e.printStackTrace();
        }
        
    }
}
