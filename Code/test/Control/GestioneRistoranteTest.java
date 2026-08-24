package Control;

import Entity.EntityMenuPizza;
import Exception.CostoNonValidoException;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GestioneRistoranteTest {
    
    public GestioneRistoranteTest() {
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

    
//--------------------------------------TEST FUNZIONE "Inserimento Pizza"---------------------------------------------
    
    @Test
    public void E1_testCostoValido () throws Exception{
        EntityMenuPizza pizza = new EntityMenuPizza (
                "ristoratore1@gmail.com",
                "margherita",
                "descrizione della pizza",
                5.50      
        );
        
        GestioneRistorante ristorante = new GestioneRistorante();
        ristorante.costoValido (pizza.getCosto());
    }
    
    @Test(expected = CostoNonValidoException.class)
    public void E2_testCostoNonValido () throws Exception{
        EntityMenuPizza pizza = new EntityMenuPizza (
                "ristoratore1@gmail.com",
                "margherita",
                "descrizione della pizza",
                -5.50       //Non valido
        );
        
        GestioneRistorante ristorante = new GestioneRistorante();
        ristorante.aggiungiPizza (pizza);
    }
    
//--------------------------------------TEST FUNZIONE "Modifica Pizza"---------------------------------------------
    
    @Test
    public void F1_testCostoValido () throws Exception{
        EntityMenuPizza pizza = new EntityMenuPizza (
                "ristoratore1@gmail.com",
                "margherita",
                "descrizione della pizza",
                5.50      
        );
        
        GestioneRistorante ristorante = new GestioneRistorante();
        ristorante.costoValido (pizza.getCosto());
    }
    
    @Test(expected = CostoNonValidoException.class)
    public void F2_testCostoNonValido () throws Exception{
        EntityMenuPizza pizza = new EntityMenuPizza (
                "ristoratore1@gmail.com",
                "margherita",
                "descrizione della pizza",
                -5.50       //Non valido
        );
        
        GestioneRistorante ristorante = new GestioneRistorante();
        ristorante.modificaPizza (pizza.getEmailRistoratore(), pizza.getNomePizza(), pizza.getDescrizione(), pizza.getCosto() );
    }
    
}
