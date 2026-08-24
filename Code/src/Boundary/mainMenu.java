package Boundary;

/**
 * Avvio del programma, porta al Boundarylogin per l'accesso dell'utente
 * @see BoundaryLogin
*/
public class mainMenu {

    public static void main(String[] args) {
        
    //Inizializzo la GUI per il login /schermata iniziale del software
        BoundaryLogin GUIiniziale = new BoundaryLogin();
        GUIiniziale.setVisible(true);
    }
    
}



