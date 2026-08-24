package Control;

import Entity.EntityCliente;
import Entity.EntityRider;
import Entity.EntityRistoratore;
import Entity.EntityUtente;

import Exception.CapNonValidoException;
import Exception.CartaNonValidaException;
import Exception.EmailNonValidaException;
import Exception.NumeroCivicoNonValidoException;
import Exception.NumeroNonValidoException;
import Exception.PasswordSbagliataException;
import Exception.UtenteNonTrovatoException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class GestioneAccessoTest {
    
    public GestioneAccessoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

//--------------------------------------TEST FUNZIONE "Login"---------------------------------------------
    
    //Test per cui l'e-mail e la password corrispondono ad un utente già presente
    @Test
    public void A1_testLoginValido() throws Exception {

        EntityUtente utentePresente = new EntityUtente(
                "esempio@esemp.io",  
                "esempio"
        );

        EntityUtente login = new EntityUtente(
                "esempio@esemp.io",
                "esempio"
        );

        if (utentePresente.getEmail() != login.getEmail()){
            throw new UtenteNonTrovatoException();
        }

        if (utentePresente.getEmail().equals(login.getEmail())){
            if (!utentePresente.getPassword().equals(login.getPassword())){
                throw new PasswordSbagliataException();
            }
        }
    }
    
    //Test per cui l'e-mail non corrisponde ad un utente già presente
    @Test (expected = UtenteNonTrovatoException.class)
    public void A2_testEmailNonPresente() throws Exception {

        EntityUtente utentePresente = new EntityUtente(
                "esempio@esemp.io",  
                "password123"
        );

        EntityUtente login = new EntityUtente(
                "ese@esemp.io", //Non presente
                "aaa"
        );

        if (utentePresente.getEmail() != login.getEmail()){
            throw new UtenteNonTrovatoException();
        }

        if (utentePresente.getEmail().equals(login.getEmail())){
            if (!utentePresente.getPassword().equals(login.getPassword())){
                throw new PasswordSbagliataException();
            }
        }
    }
    
    //Test per cui l'e-mail è presente ma la password non corrisponde
    @Test (expected = PasswordSbagliataException.class)
    public void A3_testPasswordNonCorrisponde() throws Exception {

        EntityUtente utentePresente = new EntityUtente(
                "esempio@esemp.io",  
                "esempio"
        );

        EntityUtente login = new EntityUtente(
                "esempio@esemp.io",
                "empio"
        );

        if (utentePresente.getEmail() != login.getEmail()){
            throw new UtenteNonTrovatoException();
        }

        if (utentePresente.getEmail().equals(login.getEmail())){
            if (!utentePresente.getPassword().equals(login.getPassword())){
                throw new PasswordSbagliataException();
            }
        }
    }
    
    
//--------------------------------------TEST FUNZIONE "Registrazione Cliente"---------------------------------------------

    
    //Test per cui i dati sono validi !
    @Test
    public void B1_testRegistrazioneClienteInputValidi() throws Exception {

       EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com",
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                50,
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

       GestioneAccesso gestore = new GestioneAccesso();
       
       gestore.emailValida (cliente.getEmail());
       gestore.numeroValido (cliente.getNumeroTelefonico());
       gestore.cartaValida(cliente.getNumeroCartaDiCredito());
       gestore.numeroCivicoValida (cliente.getNumeroCivico());
       gestore.capValida (cliente.getCAP());
    }
    
    //Test per cui l'e-mail del cliente non è valida
    @Test(expected = EmailNonValidaException.class)
    public void B2_testEmailNonValida() throws Exception {
        EntityCliente cliente = new EntityCliente(
                "forzanapoligmailcom",  // e-mail non valida
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                50,
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.clienteRegistrato(cliente);
    }
    
    
    //Test per cui il numero telefonico del cliente non è valida
    @Test(expected = NumeroNonValidoException.class)
    public void B3_testTelefonoNonValida() throws Exception {
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com",
                "password123",
                "Mario",
                "Rossi",
                "parappapa",        //Numero telefonico non valido
                "1234567890123456",
                50,
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.clienteRegistrato(cliente);
    }
    
    //Test per cui il numero della carta di credito del cliente non è valido
    @Test(expected = CartaNonValidaException.class)
    public void B4_testCartaDiCreditoNonValida() throws Exception {
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com",
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "aaaaaaaa", //Non Valido
                50,
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.clienteRegistrato(cliente);
    }

    //Test per cui il numero civico del cliente non è valido
    @Test(expected = CapNonValidoException.class)
    public void B5_testCapNonValido() throws Exception {
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com",
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                50,
                "Via Roma",
                "Napoli",
                "17C",
                "domenicoG" //Non Valido
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.clienteRegistrato(cliente);
    }

    //Test per cui il numero civico del cliente non è valido
    @Test(expected = NumeroCivicoNonValidoException.class)
    public void B6_testNumeroCIvicoNonValido() throws Exception {
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com",
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                50,
                "Via Roma",
                "Napoli",
                "EspositoG",  //Non valido
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.clienteRegistrato(cliente);
    }
    
    
    
//--------------------------------------TEST FUNZIONE "Registrazione Ristoratore"---------------------------------------------
    
    //Test per cui i dati sono validi !
    @Test
    public void C1_testRegistrazioneRistoratoreInputValidi() throws Exception {

       EntityRistoratore ristoratore = new EntityRistoratore(
                "forzanapoli@gmail.com", 
                "password123",
                "Mario",
                "Rossi",
                "pizzeria",
                "3897870101",
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

       GestioneAccesso gestore = new GestioneAccesso();
       
       gestore.emailValida (ristoratore.getEmail());
       gestore.numeroValido (ristoratore.getRecapitoTelefonico());
       gestore.numeroCivicoValida (ristoratore.getNumeroCivico());
       gestore.capValida (ristoratore.getCAP());
    }
    
    //Test per cui l'e-mail del ristoratore non è valida
    @Test(expected = EmailNonValidaException.class)
    public void C2_testEmailNonValida() throws Exception {
        EntityRistoratore ristoratore = new EntityRistoratore(
                "forzanapoligmailcom",  // e-mail non valida
                "password123",
                "Mario",
                "Rossi",
                "pizzeria",
                "3897870101",
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.ristoratoreRegistrato(ristoratore);
    }
    
    
    //Test per cui il numero telefonico del ristoratore non è valida
    @Test(expected = NumeroNonValidoException.class)
    public void C3_testTelefonoNonValida() throws Exception {
        EntityRistoratore ristoratore = new EntityRistoratore(
                "forzanapoli@gmail.com",  // email valida
                "password123",
                "Mario",
                "Rossi",
                "pizzeria",
                "3897870101333",   //Non Valido
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.ristoratoreRegistrato(ristoratore);
    }
    
    //Test per cui il CAP del ristoratore non è valido
    @Test(expected = CapNonValidoException.class)
    public void C4_testCapNonValida() throws Exception {
        EntityRistoratore ristoratore = new EntityRistoratore(
                "forzanapoli@gmail.com",  
                "password123",
                "Mario",
                "Rossi",
                "pizzeria",
                "3897870101",
                "Via Roma",
                "Napoli",
                "17C",
                "domenicoG"     //Non valido
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.ristoratoreRegistrato(ristoratore);
    }

    //Test per cui il numero civico del ristoratore non è valido
    @Test(expected = CapNonValidoException.class)
    public void C5_testNumeroCivicolNonValida() throws Exception {
        EntityRistoratore ristoratore = new EntityRistoratore(
                "forzanapoli@gmail.com", 
                "password123",
                "Mario",
                "Rossi",
                "pizzeria",
                "3897870101",
                "Via Roma",
                "Napoli",
                "17C",
                "EspositoG"     //Non valido
        );

        GestioneAccesso gestione = new GestioneAccesso();
        gestione.ristoratoreRegistrato(ristoratore);
    }

    
//--------------------------------------TEST FUNZIONE "Inserimento Rider"---------------------------------------------
    
    //Test per cui i dati sono validi !
    @Test
    public void D1_testRegistrazioneRiderInputValidi() throws Exception {

        EntityRider rider = new EntityRider (
                "forzanapoli@gmail.com", 
                "password123",
                "Mario",
                "Rossi",
                "80059",
                "libero"
        );

       GestioneAccesso gestore = new GestioneAccesso();
       
       gestore.emailValida (rider.getEmail());
       gestore.capValida (rider.getCAPDiLavoro());
    }
    
    //Test per cui l'e-mail del rider non è valida
    @Test(expected = EmailNonValidaException.class)
    public void D2_testEmailNonValida ()throws Exception{
        EntityRider rider = new EntityRider (
                "forzanapoligmailcom",  //Non valida
                "password123",
                "Mario",
                "Rossi",
                "80059",
                "libero"
        );
                
        GestioneAccesso gestione = new GestioneAccesso();
        gestione.riderRegistrato(rider);
    }
    
    //Test per cui il CAP del rider non è valido
    @Test(expected = CapNonValidoException.class)
    public void D3_testCapNonValido ()throws Exception{
        EntityRider rider = new EntityRider (
                "forzanapoli@gmail.com",  
                "password123",
                "Mario",
                "Rossi",
                "domenicoG",
                "libero"
        );
                
        GestioneAccesso gestione = new GestioneAccesso();
        gestione.riderRegistrato(rider);
    }
    
}
