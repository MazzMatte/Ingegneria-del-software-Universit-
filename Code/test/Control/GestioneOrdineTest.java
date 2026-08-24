package Control;

import Entity.EntityCliente;
import Entity.EntityOrdinePizza;
import Exception.CreditoNonSufficienteException;
import Exception.QuantitaNonValidaException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GestioneOrdineTest {
    
    public GestioneOrdineTest() {
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

//--------------------------------------TEST FUNZIONE "Effettua Ordine"---------------------------------------------

    @Test
    public void G1_testInputValidi() throws Exception{
        EntityOrdinePizza pizza = new EntityOrdinePizza(
                1,
                "Margherita",
                3
        );
        double costo = 3;
        
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com", 
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                300,          
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );
        
        double creditoResiduo = cliente.getCredito() - costo * pizza.getQuantita();
        
        if ( creditoResiduo < 0){
            throw new CreditoNonSufficienteException();
        }
        
        GestioneOrdine gestore = new GestioneOrdine();
        gestore.quantitaValido (pizza.getQuantita());
    }
    
    @Test(expected = QuantitaNonValidaException.class)
    public void G2_testQuantitaNonValida() throws Exception{
        EntityOrdinePizza pizza = new EntityOrdinePizza(
                1,
                "Margherita",
                -2  //Non Valido
        );
        
        GestioneOrdine gestore = new GestioneOrdine();
        gestore.quantitaValido (pizza.getQuantita());
    }
    
    @Test(expected = CreditoNonSufficienteException.class)
    public void G3_testCreditoInsufficiente() throws Exception{
        
        EntityCliente cliente = new EntityCliente(
                "forzanapoli@gmail.com", 
                "password123",
                "Mario",
                "Rossi",
                "3897870101",
                "1234567890123456",
                2,          //Credito basso
                "Via Roma",
                "Napoli",
                "17C",
                "80059"
        );
        ArrayList<String> listaOrdine = new ArrayList<>();
        listaOrdine.add("Margherita;3");
        
        GestioneOrdine gestore = new GestioneOrdine();
        gestore.inserisciOrdine (cliente.getEmail(), "ristoratore1@gmail.com", listaOrdine);
    }
    
}
