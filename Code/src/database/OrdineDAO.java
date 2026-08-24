package database;

/**
 * Agisce sulla tabella fisica del Database "Ordini".
 */

import Entity.EntityOrdine;

import java.sql.*;
import java.time.*;
import java.util.*;

public class OrdineDAO {

    private Connection conn;                         

    /**
     * Costruttore che inizializza la connessione al database tramite DBManager.
     * 
     * @throws SQLException in caso di problemi di connessione
     */
    public OrdineDAO() throws SQLException{
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
   
   
//--------------------------------------------------CONSEGNE (RIDER)---------------------------------------------------    

    /**
     * Ottiene un ordine per un rider in base al CAP di lavoro.
     * Se il rider ha già un ordine preso in carico (stato "pronta consegna") lo restituisce.
     * Altrimenti assegna al rider il primo ordine "in attesa" disponibile per il CAP specificato.
     * 
     * @param CAPDiLavoro CAP dove il rider lavora
     * @param emailRider email identificativa del rider
     * @return EntityOrdine l'ordine assegnato o null se non ci sono ordini disponibili
     * @throws SQLException in caso di errori nell'accesso al database
     */
    public EntityOrdine ottieniOrdine(String CAPDiLavoro, String emailRider) throws SQLException {
        EntityOrdine ordine = null;

        // Verifico se il Rider ha già una consegna presa in carico (NON può prendere in carico più ordini contemporaneamente)
        String queryEsistente = "SELECT * FROM ordini WHERE stato = 'pronta consegna' AND emailRider = ?";
        PreparedStatement stmtEsistente = conn.prepareStatement(queryEsistente);
        stmtEsistente.setString(1, emailRider);
        ResultSet rsEsistente = stmtEsistente.executeQuery();

        // Se il rider ha già un ordine in carico
        if (rsEsistente.next()) {
            ordine = new EntityOrdine();
            ordine.setCodiceUnivoco(rsEsistente.getInt("codiceUnivoco"));
            ordine.setOraProntaConsegna(rsEsistente.getTime("oraProntaConsegna"));
            ordine.setOraFineConsegna(rsEsistente.getTime("oraFineConsegna"));
            ordine.setCostoTotale(rsEsistente.getDouble("costoTotale"));
            ordine.setDataOrdine(rsEsistente.getDate("dataOrdine"));
            ordine.setEmailCliente(rsEsistente.getString("emailCliente"));
            ordine.setEmailRistoratore(rsEsistente.getString("emailRistoratore"));
            ordine.setEmailRider(emailRider);

            return ordine; // ritorna l'ordine già preso in carico
        }

        // Se non ha già una consegna presa in carico trovo il primo ordine disponibile
        String query = "SELECT o.* FROM ordini o " +
                       "JOIN clienti c ON o.emailCliente = c.email " +
                       "WHERE o.stato = 'in attesa' AND c.CAP = ? LIMIT 1";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, CAPDiLavoro);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            ordine = new EntityOrdine();
            ordine.setCodiceUnivoco(rs.getInt("codiceUnivoco"));
            ordine.setOraProntaConsegna(rs.getTime("oraProntaConsegna"));
            ordine.setOraFineConsegna(rs.getTime("oraFineConsegna"));
            ordine.setCostoTotale(rs.getDouble("costoTotale"));
            ordine.setDataOrdine(rs.getDate("dataOrdine"));
            ordine.setEmailCliente(rs.getString("emailCliente"));
            ordine.setEmailRistoratore(rs.getString("emailRistoratore"));
            ordine.setEmailRider(emailRider);

            // Aggiorna l'ordine per assegnare il rider e cambiare stato
            String updateOrdine = "UPDATE ordini SET stato = 'pronta consegna', emailRider = ?, oraProntaConsegna = ? WHERE codiceUnivoco = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateOrdine);
            updateStmt.setString(1, emailRider);
            updateStmt.setTime(2, new Time(System.currentTimeMillis())); // ora attuale
            updateStmt.setInt(3, ordine.getCodiceUnivoco());
            updateStmt.executeUpdate();
        }

        return ordine;
    }

    /**
     * Conferma la consegna di un ordine aggiornandone lo stato a "fine consegna"
     * e impostando l'ora di fine consegna.
     * 
     * @param codiceOrdine codice univoco dell'ordine da confermare
     * @return true se l'aggiornamento è andato a buon fine, false altrimenti
     * @throws SQLException in caso di errori nell'accesso al database
     */
    public boolean confermaConsegna(int codiceOrdine) throws SQLException {
        
        String updateOrdine = "UPDATE ordini SET stato = 'fine consegna', oraFineConsegna = ? WHERE codiceUnivoco = ?";
        
        PreparedStatement stmt = conn.prepareStatement(updateOrdine);
        stmt.setTime(1, new Time(System.currentTimeMillis())); // ora attuale locale
        stmt.setInt(2, codiceOrdine);

        int rowsUpdated = stmt.executeUpdate();

        return rowsUpdated > 0;
    }

    
//------------------------------------------------ORDINI (RISTORATORE)------------------------------------------------------    
    
    /**
     * Restituisce la lista degli ordini che il ristoratore con l'email fornita deve svolgere.
     * Vengono considerati gli ordini con stato "in attesa" o "pronta consegna".
     * 
     * @param emailRistoratore email del ristoratore
     * @return ArrayList di EntityOrdine contenente gli ordini del ristoratore
     * @throws SQLException in caso di errori nell'accesso al database
     */
    public ArrayList<EntityOrdine> ottieniOrdini(String emailRistoratore)throws SQLException{
        
        ArrayList<EntityOrdine> ordini = new ArrayList<>();
        
        int codiceUnivoco;
        String emailCliente;
                
        try{
            String query = "SELECT * FROM ordini WHERE emailRistoratore = ? AND (stato = 'in attesa' OR stato = 'pronta consegna');";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, emailRistoratore);
            ResultSet rs = stmt.executeQuery();
            
            // Costruisco la lista degli ordini per emailRistoratore
            while (rs.next()){
                codiceUnivoco = rs.getInt("codiceUnivoco");
                emailCliente = rs.getString("emailCliente");
                
                // Aggiungo l'ordine nella lista
                ordini.add(new EntityOrdine(codiceUnivoco, null, null, 0, null, null, emailCliente, null, null));
            }
        } catch(SQLException e){
            System.out.println("Connessione fallita! (ottieniOrdini)");
            e.printStackTrace();
        } 
                
        return ordini;
    }
          
//---------------------------------------------------REPORT  (GESTORE)----------------------------------------------------------    
 
    /**
     * Ottiene un report dei tempi medi di consegna e del numero totale di ordini per CAP, dati mese e anno.
     * Calcola la media della differenza tra oraFineConsegna e oraProntaConsegna per ogni CAP
     * e conta il numero di ordini.
     *
     * @param mese mese di riferimento (1-12)
     * @param anno anno di riferimento (es. 2025)
     * @return lista di Object[] con CAP (String), durata media (Duration) e numero ordini (Integer)
     * @throws SQLException in caso di errori nell'accesso al database
     */
    public List<Object[]> getReportPerMeseAnno(int mese, int anno) throws SQLException {

        List<Object[]> report = new ArrayList<>();

        try {
            String query = """
                    SELECT c.CAP,
                           SEC_TO_TIME(
                             AVG(
                               CASE
                                 WHEN o.oraFineConsegna >= o.oraProntaConsegna THEN
                                   TIME_TO_SEC(TIMEDIFF(o.oraFineConsegna, o.oraProntaConsegna))
                                 ELSE
                                   TIME_TO_SEC(TIMEDIFF(ADDTIME(o.oraFineConsegna, '24:00:00'), o.oraProntaConsegna))
                               END
                             )
                           ) AS tempo_medio,
                           COUNT(*) AS numero_ordini
                    FROM ordini o
                    JOIN clienti c ON o.emailCliente = c.email
                    WHERE MONTH(o.dataOrdine) = ? AND YEAR(o.dataOrdine) = ?
                    GROUP BY c.CAP;
                    """;

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, mese);
            stmt.setInt(2, anno);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String cap = rs.getString("CAP");
                Time tempoMedioSQL = rs.getTime("tempo_medio");
                int numeroOrdini = rs.getInt("numero_ordini"); // Estrazione del numero di ordini

                if (tempoMedioSQL != null) {
                    Duration durata = Duration.ofHours(tempoMedioSQL.toLocalTime().getHour())
                            .plusMinutes(tempoMedioSQL.toLocalTime().getMinute())
                            .plusSeconds(tempoMedioSQL.toLocalTime().getSecond());

                    report.add(new Object[]{cap, durata, numeroOrdini});
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Connessione fallita! (ottenimento Report)");
            e.printStackTrace();
        }

        return report;
    }

//------------------------------------------------ORDINE (CLIENTE)------------------------------------------------------

    /**
     * Aggiunge un nuovo ordine al database e restituisce il codice univoco generato.
     * Lo stato iniziale dell'ordine è "in attesa".
     * 
     * @param emailCliente email del cliente che effettua l'ordine
     * @param emailRistoratore email del ristoratore a cui è destinato l'ordine
     * @param costoTotale costo totale dell'ordine
     * @param dataOrdine data dell'ordine
     * @param oraProntaConsegna orario previsto per la preparazione
     * @return codice univoco generato per l'ordine, -1 se fallisce
     * @throws SQLException in caso di errori nell'accesso al database
     */
    public int aggiungiOrdine(String emailCliente, String emailRistoratore, double costoTotale, LocalDate dataOrdine, LocalTime oraProntaConsegna) throws SQLException{
        
        int codiceUnivoco = -1;
        
        try{
            
            // Inizializzo statoOrdine a "in attesa"
            String query = "INSERT INTO ordini (dataOrdine, oraProntaConsegna, costoTotale, emailCliente, emailRistoratore, stato) VALUES (?, ?, ?, ?, ? , 'in attesa')";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, java.sql.Date.valueOf(dataOrdine));
            stmt.setTime(2, java.sql.Time.valueOf(oraProntaConsegna));
            stmt.setDouble(3, costoTotale);
            stmt.setString(4, emailCliente);
            stmt.setString(5, emailRistoratore);
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                codiceUnivoco = generatedKeys.getInt(1);
            }
            
        } catch(SQLException e){
            System.out.println("Connessione fallita! (aggiungiOrdine in OrdineDAO)");
            e.printStackTrace();
        }
    
        return codiceUnivoco;
    }
    
}
