package database;

/**
 * Classe DAO che agisce sulla tabella fisica "Ordine_pizza" del database.
 */

import Entity.EntityOrdinePizza;
import Entity.EntityOrdine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

public class OrdinePizzaDAO {
    
    private Connection conn;
    
    /**
     * Costruttore della classe OrdinePizzaDAO.
     * Stabilisce una connessione al database tramite DBManager.
     * 
     * @throws SQLException se la connessione al database fallisce
     */
    public OrdinePizzaDAO() throws SQLException{
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
//---------------------------------------------ORDINI PER IL RISTORATORE---------------------------------------------------
    
    /**
     * Ottiene la lista delle pizze ordinate associata ad una lista di ordini.
     * Per ogni ordine passato (con codice univoco), vengono estratti nomePizza e quantità.
     * 
     * @param ordini lista di oggetti EntityOrdine contenenti gli ordini da cui estrarre le pizze
     * @return ArrayList di EntityOrdinePizza con le pizze ordinate e relative quantità per ogni ordine
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public ArrayList<EntityOrdinePizza> ottieniOrdinePizze(ArrayList<EntityOrdine> ordini) throws SQLException{
        
        ArrayList<EntityOrdinePizza> ordinePizza = new ArrayList<>();
        
        String nomePizza;
        int codiceUnivoco, quantita;
        
        try{
            
            // Scorro tutto l'ArrayList ordini (contenente i codici)
            for (EntityOrdine o : ordini){
                String query = "SELECT * FROM ordine_pizza WHERE codiceOrdine = ? ORDER BY codiceOrdine";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, o.getCodiceUnivoco());
                
                ResultSet rs = stmt.executeQuery();
                
                // Scorro tutte le righe restituite con la query
                while (rs.next()){
                    codiceUnivoco = o.getCodiceUnivoco();
                    nomePizza = rs.getString("nomePizza");
                    quantita = rs.getInt("quantità");
                    
                    // aggiungo nell'arrayList
                    ordinePizza.add(new EntityOrdinePizza(codiceUnivoco, nomePizza, quantita));
                }
            }
            
        } catch(SQLException e){
            System.out.println("Connessione fallita! (ottieniOrdinePizze in OrdinePizzaDAO)");
            e.printStackTrace();
        }        
        
        return ordinePizza;
    }
    
//--------------------------------------------ORDINE DEL CLIENTE--------------------------------------
    
    /**
     * Aggiunge una nuova riga nella tabella ordine_pizza per associare una pizza e la quantità ad un ordine specifico.
     * 
     * @param codiceOrdine codice univoco dell'ordine a cui associare la pizza
     * @param nomePizza nome della pizza da aggiungere all'ordine
     * @param quantita quantità della pizza ordinata
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    public boolean aggiungiOrdinePizza(int codiceOrdine, String nomePizza, int quantita) throws SQLException{
        
        boolean aggiunto = false;
        
        try{
            
            String query = "INSERT INTO ordine_pizza (codiceOrdine, nomePizza, quantità) VALUES (?, ?, ?);";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, codiceOrdine);
            stmt.setString(2, nomePizza);
            stmt.setInt(3, quantita);
            
            int righeInserite = stmt.executeUpdate();
            if(righeInserite > 0){
                aggiunto = true;	
            }
            
        } catch(SQLException e){
            System.out.println("Connessione fallita! (aggiungiOrdinePizza in OrdinePizzaDAO)");
            e.printStackTrace();
        }
        
        return aggiunto;
    }
    
}
