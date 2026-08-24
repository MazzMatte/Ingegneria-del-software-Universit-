package database;

/*
Per la connessione al database (Connessione e chiusura al database)
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    
    private static Connection conn = null;
    
    /**
     * Costruttore privato per impedire l'istanza della classe.
     */
    private DBManager() {
    }
    
    /**
     * Restituisce una connessione attiva al database.
     * Se non esiste una connessione o se è chiusa, ne apre una nuova.
     * 
     * @return Connessione al database MySQL pizzaacasa.
     * @throws SQLException se la connessione al database fallisce.
     */
    public static Connection getConnection() throws SQLException {		
        if(conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzaacasa", "root", "");
        }
        return conn;
    }
    
    /**
     * Chiude la connessione al database se esiste ed è aperta.
     * 
     * @return Connessione al database MySQL pizzaacasa.
     * @throws SQLException se la chiusura della connessione fallisce.
     */
    public static Connection closeConnection() throws SQLException {
        if(conn != null) {
            conn.close();
        }
        return conn;
    }
}
