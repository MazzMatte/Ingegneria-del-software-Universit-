package Control;

import database.ClienteDAO;
import database.GestoreDAO;
import database.RiderDAO;
import database.RistoratoreDAO;
import database.UtenteDAO;

import Entity.EntityUtente;
import Entity.EntityCliente;
import Entity.EntityRistoratore;
import Entity.EntityRider;
import Entity.EntityGestore;

import Exception.CapNonValidoException;
import Exception.CartaNonValidaException;
import Exception.EmailNonValidaException;
import Exception.NumeroCivicoNonValidoException;
import Exception.NumeroNonValidoException;

import Exception.UtenteGiaPresenteException;

import Exception.UtenteNonTrovatoException;
import Exception.PasswordSbagliataException;


import java.sql.SQLException;

/**
 * Gestisce l'accesso al sito tramite il BoundaryLogin.
 * Si occupa della gestione dell'autenticazione e della registrazione dei vari utenti,
 * instradandoli alle rispettive aree personali.
 */
public class GestioneAccesso {
    
//------------------------------------LOGIN------------------------------------------
    
    /**
     * Restituisce l'utente che è registrato nel sistema con l'email inserita.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @param password Password inseritsa dell'utente inserita in fase di login
     * @return Oggetto variabile (tra EntityRistoratore, EntityCliente,EntityRider o EntityGestore), altrimenti -1 0 -2
     * @throws SQLException In caso di problemi di connessione al database
     * @throws Exception.UtenteNonTrovatoException
     * @throws Exception.PasswordSbagliataException
    */
    public Object loginUtente (String email, String password)throws SQLException, UtenteNonTrovatoException, PasswordSbagliataException {
        EntityUtente utente = verificaUtente(email);
        if (utente == null) {
            //Utente insesistente
            //return -1;          //torna -1 se l'email non è presente
            throw new UtenteNonTrovatoException();
        }   
        if (!utente.getPassword().equals(password)){
            //password inesistente
            //return -2;          //torna -2 email esistente ma password NON combacia
            throw new PasswordSbagliataException();
        }
        
        EntityCliente cliente = verificaCliente(email, password);
        if (cliente != null){ 
            return cliente;
        }

        EntityRistoratore ristoratore = verificaRistoratore(email, password);
        if (ristoratore != null){ 
            return ristoratore;
        }

        EntityRider rider = verificaRider(email, password);
        if (rider != null) {
            return rider;
        }

        EntityGestore gestore = verificaGestore(email, password);
        if (gestore != null) {
            return gestore;
        }

        return utente; //Torna email / password
    }
    
    
    /**
     * Verifica se un utente è presente e registrato nel sistema.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @return Oggetto EntityUtente se l'utente è trovato, null altrimenti
     * @throws SQLException In caso di problemi di connessione al database
    */
    private EntityUtente verificaUtente(String email) throws SQLException {
        UtenteDAO utente = new UtenteDAO();
        
        EntityUtente user = utente.leggiUtente(email);
        utente.close();
        
        return user;
    }
    
    /**
     *  Verifica se l'utente sia di tipo Cliente.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @param password Password dell'utente inserita in fase di login
     * @return Oggetto EntityCliente se è un cliente registrato, null altrimenti
     * @throws SQLException In caso di problemi di connessione al database
    */
    private EntityCliente verificaCliente(String email, String password) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        
        EntityCliente user = clienteDAO.leggiCliente(email, password);
        clienteDAO.close();
        
        return user;
    }
    
    /**
     *  Verifica se l'utente sia di tipo Ristoratore.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @param password Password dell'utente inserita in fase di login
     * @return Oggetto EntityRistoratore se è un ristoratore registrato, null altrimenti
     * @throws SQLException In caso di problemi di connessione al database
    */
    private EntityRistoratore verificaRistoratore(String email, String password) throws SQLException {
        RistoratoreDAO ristoratoreDAO = new RistoratoreDAO();
        
        EntityRistoratore user = ristoratoreDAO.leggiRistoratore(email, password);
        ristoratoreDAO.close();
        
        return user;
    }
    
    /**
     * Verifica se l'utente sia di tipo Rider.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @param password Password dell'utente inserita in fase di login
     * @return Oggetto EntityRider se è un rider registrato, null altrimenti
     * @throws SQLException In caso di problemi di connessione al database
    */
    private EntityRider verificaRider(String email, String password) throws SQLException {
        RiderDAO riderDAO = new RiderDAO();
        
        EntityRider user = riderDAO.leggiRider(email, password);
        riderDAO.close();
        
        return user;
    }
    
    /**
     * Verifica se l'utente sia di tipo Gestore.
     * 
     * @param email Email dell'utente inserita in fase di login
     * @param password Password dell'utente inserita in fase di login
     * @return Oggetto EntityGestore se è un gestore registrato, null altrimenti
     * @throws SQLException In caso di problemi di connessione al database
    */
    private EntityGestore verificaGestore(String email, String password) throws SQLException {
        GestoreDAO gestoreDAO = new GestoreDAO();
        
        EntityGestore user = gestoreDAO.leggiGestore(email, password);
        gestoreDAO.close();
        
        return user;
    }
    
//------------------------------------------REGISTRAZIONE--------------------------------------------------
    
    /**
     * Registra un nuovo Ristoratore.
     * 
     * @param nuovoRistoratore dati del nuovo ristoratore da inserire nel database
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * @throws SQLException In caso di problemi di connessione al database
     * @throws Exception.UtenteGiaPresenteException In caso di Utente già presente nel sistema con quella e-mail
     * @throws Exception.CapNonValidoException In caso di input non valido / input atteso
     * @throws Exception.EmailNonValidaException In caso di input non valido / input atteso
     * @throws Exception.NumeroCivicoNonValidoException In caso di input non valido / input atteso
     * @throws Exception.NumeroNonValidoException In caso di input non valido / input atteso
    */    
    public boolean ristoratoreRegistrato(EntityRistoratore nuovoRistoratore) throws SQLException, UtenteGiaPresenteException, CapNonValidoException, EmailNonValidaException, NumeroCivicoNonValidoException, NumeroNonValidoException {
        
        UtenteDAO utenteDAO = new UtenteDAO ();
        
        emailValida (nuovoRistoratore.getEmail());
        capValida (nuovoRistoratore.getCAP());
        numeroValido (nuovoRistoratore.getRecapitoTelefonico());
        numeroCivicoValida (nuovoRistoratore.getNumeroCivico());
        
        if (utenteDAO.leggiUtente (nuovoRistoratore.getEmail()) == null){
            //Utente non esistente (con quella email)
            
            if (utenteDAO.inserisciUtente ( nuovoRistoratore.getEmail(), nuovoRistoratore.getPassword()) == true ){
                
                //Se è stato inserito nel database, tabella "utenti"
                RistoratoreDAO ristoratoreDAO = new RistoratoreDAO();
                ristoratoreDAO.aggiungiRistoratore (nuovoRistoratore);
                
                utenteDAO.close();
                ristoratoreDAO.close();
                return true;
            }
        } else{
            throw new UtenteGiaPresenteException();
        }
        
        utenteDAO.close();
        return false;
    }
 
    
    /**
     * Registra un nuovo Cliente.
     * 
     * @param nuovoCliente dati del nuovo cliente da inserire nel database
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * @throws SQLException In caso di problemi di connessione al database
     * @throws Exception.CapNonValidoException In caso di input non valido / input atteso
     * @throws Exception.CartaNonValidaException In caso di input non valido / input atteso
     * @throws Exception.EmailNonValidaException In caso di input non valido / input atteso
     * @throws Exception.NumeroCivicoNonValidoException In caso di input non valido / input atteso
     * @throws Exception.NumeroNonValidoException In caso di input non valido / input atteso
     * @throws Exception.UtenteGiaPresenteException In caso di Utente già presente nel sistema con quella e-mail
    */
    public boolean clienteRegistrato( EntityCliente nuovoCliente) throws SQLException, CapNonValidoException, CartaNonValidaException, EmailNonValidaException, NumeroCivicoNonValidoException, NumeroNonValidoException, UtenteGiaPresenteException{
        
        UtenteDAO utenteDAO = new UtenteDAO ();
        
        emailValida (nuovoCliente.getEmail());
        capValida (nuovoCliente.getCAP());
        numeroValido (nuovoCliente.getNumeroTelefonico());
        cartaValida (nuovoCliente.getNumeroCartaDiCredito());
        numeroCivicoValida (nuovoCliente.getNumeroCivico());
            
        if (utenteDAO.leggiUtente (nuovoCliente.getEmail()) == null){
            //Utente non esistente (con quella email)
            
            if (utenteDAO.inserisciUtente ( nuovoCliente.getEmail(), nuovoCliente.getPassword()) == true ){
                
                //Se è stato inserito nel database, tabella "utenti"
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.aggiungiCliente (nuovoCliente);
                
                clienteDAO.close();
                utenteDAO.close();
                return true;
            }
        }else{
            throw new UtenteGiaPresenteException();
        } 
        
        utenteDAO.close();
        return false;
    }
   
    
     /**
     * Registra un nuovo Rider.
     * 
     * @param nuovoRider dati del nuovo rider da inserire nel database
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * @throws SQLException In caso di problemi di connessione al database
     * @throws Exception.EmailNonValidaException In caso di inserimento di una mail non valida
     * @throws Exception.CapNonValidoException In caso di inserimento di un CAP non valio
    */
    public boolean riderRegistrato( EntityRider nuovoRider) throws SQLException, EmailNonValidaException, CapNonValidoException {
        
        UtenteDAO utenteDAO = new UtenteDAO ();
        
        emailValida (nuovoRider.getEmail());
        capValida (nuovoRider.getCAPDiLavoro());
        
        
        if (utenteDAO.leggiUtente (nuovoRider.getEmail()) == null){
            //Utente non esistente (con quella email)
            
            if (utenteDAO.inserisciUtente ( nuovoRider.getEmail(), nuovoRider.getPassword()) == true ){
                
                //Se è stato inserito nel database, tabella "utenti"
                RiderDAO riderDAO = new RiderDAO();
                riderDAO.aggiungiRider (nuovoRider);
                
                riderDAO.close();
                utenteDAO.close();
                return true;
            }
        } 
        
        utenteDAO.close();
        return false;
    }
   
//------------------------VALIDAZIONE DEI DATI-------------------------------
    
    
    /**
     * Verfica che l'e-mail sia valida.
     * 
     * @param email E-mail da verificare
     * @throws Exception.EmailNonValidaException In caso l'E-Mail inserito non sia valida (No @ .)
    */
    public void emailValida (String email) throws EmailNonValidaException{
           
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                throw new EmailNonValidaException();
            }
        
    }
    
    /**
     * Verfica che il numero telefonico sia valido.
     * 
     * @param numero numero telefonico da verificare
     * @throws Exception.NumeroNonValidoException In caso il numero telefonico inserito non sia valida (No ha 10 cifre)
    */
    public void numeroValido (String numero) throws NumeroNonValidoException{
        
        if (!numero.matches("\\d{10}")) {
                throw new NumeroNonValidoException();
            }
    }
         
    /**
     * Verfica che il numero della carta di credito sia valido.
     * 
     * @param cartaDiCredito numero della carta di credito da verificare
     * @throws Exception.NumeroNonValidoException In caso il numero telefonico inserito non sia valida (No ha 16 cifre)
    */
    public void cartaValida (String cartaDiCredito )  throws CartaNonValidaException{
        
        if (!cartaDiCredito.matches("\\d{16}")) {
                throw new CartaNonValidaException();
        }
        
    }
    
    /**
     * Verfica che il numero telefonico sia valido.
     * 
     * @param n_civico numero civico da verificare
     * @throws Exception.NumeroCivicoNonValidoException In caso che il numero civico inserito non sia valio (Nessun numero presente)
    */
    public void numeroCivicoValida (String n_civico) throws NumeroCivicoNonValidoException{
        
        if (!n_civico.matches(".*\\d.*")) {
                throw new NumeroCivicoNonValidoException();
            }
        
    }
    
    /**
     * Verfica che il CAP sia valido.
     * 
     * @param cap CAP da verificare
     * @throws Exception.CapNonValidoException In caso il CAP inserito non sia valida (Ha 5 cifre)
    */
    public void capValida (String cap) throws CapNonValidoException{
        
        if (!cap.matches("\\d{5}")) {
                //JOptionPane.showMessageDialog(this, "CAP non valido ( deve avere 5 cifre )", "Errore", JOptionPane.ERROR_MESSAGE);
                throw new CapNonValidoException();
            }
        
    }
    
    
    
}
