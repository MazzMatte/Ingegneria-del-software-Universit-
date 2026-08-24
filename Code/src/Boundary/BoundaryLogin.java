package Boundary;

import Control.GestioneAccesso;

import Entity.EntityCliente;
import Entity.EntityGestore;
import Entity.EntityRider;
import Entity.EntityRistoratore;
import Exception.CapNonValidoException;
import Exception.CartaNonValidaException;
import Exception.EmailNonValidaException;
import Exception.NumeroCivicoNonValidoException;
import Exception.NumeroNonValidoException;
import Exception.UtenteNonTrovatoException;
import Exception.PasswordSbagliataException;
import Exception.UtenteGiaPresenteException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta l'interfaccia grafica iniziale dell'utente di Pizza@Casa.
 * Permette ad un Utente di accedere nella propria Area Personale del sistema Pizza@Casa.
 * 
 * Precisamente un utente può:
 * <ul>
 *  <li>Effettuare il Login al sistema, accedendo con credenziali già registrate in precedenza.</li>
 *  <li>Registrarsi in Pizza@Casa come cliente.</li>
 *  <li>Registrarsi in Pizza@Casa come ristoratore.</li>
 * </ul>
 */
public class BoundaryLogin extends JFrame {
    private final JPanel mainPanel;
    private JTextField emailField;
    private JPasswordField passwordField;

    /**
     * Costruttore che inizializza la finestra di login con titolo, dimensioni e posizionamento.
     * Crea il pannello principale e mostra la schermata di login.
     */
    public BoundaryLogin() {
        setTitle("Pizza@Casa - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        mostraLogin();
    }

    /**
     * Mostra la schermata di login con campi email, password e pulsanti per accedere o registrarsi.
     * Configura anche le azioni associate ai pulsanti.
     */
    private void mostraLogin() {
        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        JLabel titleLabel = new JLabel("Pizza@Casa - Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(titleLabel);

        Dimension fieldSize = new Dimension(2250, 30);

        emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);

        loginPanel.add(creaCampo("Email:", emailField));
        loginPanel.add(creaCampo("Password:", passwordField));

        //Tasto "accedi"
        JButton loginButton = new JButton("Accedi");
        loginButton.addActionListener(e -> {
            try {
                login();
            } catch (SQLException ex) {
                Logger.getLogger(BoundaryLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UtenteNonTrovatoException ex) {
                JOptionPane.showMessageDialog(this, "Utente non registrato al sistema.");            
            } catch (PasswordSbagliataException ex) {
                JOptionPane.showMessageDialog(this, "Password errata.");
            }
        });    
        loginPanel.add(loginButton);

        JPanel registrazionePanel = new JPanel(new FlowLayout());
        
        //Tasto "Registrati come Cliente"
        JButton clienteBtn = new JButton("Registrati come Cliente");
        clienteBtn.addActionListener(e -> mostraRegistrazioneCliente());
        
        //Tasto "Registrati come Ristoratore"
        JButton ristoratoreBtn = new JButton("Registrati come Ristoratore");
        ristoratoreBtn.addActionListener(e -> mostraRegistrazioneRistoratore());

        registrazionePanel.add(clienteBtn);
        registrazionePanel.add(ristoratoreBtn);

        loginPanel.add(registrazionePanel);

        mainPanel.removeAll();
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Effettua il login verificando le credenziali inserite dall'utente.
     * Se il login ha successo, apre la corrispondente area personale dell'utente in base al suo ruolo.
     * 
     * @throws SQLException in caso di errori di accesso al database.
     * @throws UtenteNonTrovatoException in caso di utente non presente.
     * @throws PasswordSbagliataException in caso dipassword sbagliata.
     */
    private void login() throws SQLException, UtenteNonTrovatoException, PasswordSbagliataException {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci email e password!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GestioneAccesso gestione = new GestioneAccesso();
        
        Object risultato = gestione.loginUtente(email, password);

        // Risultato è un'entità
        if (risultato instanceof EntityCliente cliente) {
            dispose();
            new BoundaryCliente(cliente).setVisible(true);
        } else if (risultato instanceof EntityRistoratore ristoratore) {
            dispose();
            new BoundaryRistoratore(ristoratore).setVisible(true);
        } else if (risultato instanceof EntityRider rider) {
            dispose();
            new BoundaryRider(rider).setVisible(true);
        } else if (risultato instanceof EntityGestore) {
            dispose();
            new BoundaryGestore().setVisible(true);
        }
        
    }
    
//----------------------------------------REGISTRAZIONE------------------------------------------
    
    /**
     * Mostra la schermata di registrazione per un nuovo Cliente.
     * Il cliente deve inserire email, password, dati anagrafici, dati di contatto, dati di pagamento e indirizzo.
     * Verifica la validità dei dati inseriti e tenta di registrare il cliente nel sistema.
     */
    private void mostraRegistrazioneCliente() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField nome = new JTextField();
        JTextField cognome = new JTextField();
        JTextField telefono = new JTextField();
        JTextField numeroCarta = new JTextField();
        JTextField via = new JTextField();
        JTextField citta = new JTextField();
        JTextField civico = new JTextField();
        JTextField cap = new JTextField();

        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(new JLabel("Nome:")); panel.add(nome);
        panel.add(new JLabel("Cognome:")); panel.add(cognome);
        panel.add(new JLabel("Telefono:")); panel.add(telefono);
        panel.add(new JLabel("Numero Carta:")); panel.add(numeroCarta);
        panel.add(new JLabel("Via:")); panel.add(via);
        panel.add(new JLabel("Città:")); panel.add(citta);
        panel.add(new JLabel("Civico:")); panel.add(civico);
        panel.add(new JLabel("CAP:")); panel.add(cap);

        JButton registra = new JButton("Conferma Registrazione Cliente");
        registra.addActionListener(e -> {
            //Registra il Cliente
                //Verifico che tutti i campi siano "pieni"
            if (email.getText().isEmpty() || new String(password.getPassword()).isEmpty() ||
                nome.getText().isEmpty() || cognome.getText().isEmpty() || telefono.getText().isEmpty() ||
                numeroCarta.getText().isEmpty() || via.getText().isEmpty() ||
                citta.getText().isEmpty() || civico.getText().isEmpty() || cap.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            try {
                EntityCliente user = new EntityCliente (email.getText(), 
                        new String(password.getPassword()), 
                        nome.getText(), 
                        cognome.getText(),
                        telefono.getText(), 
                        numeroCarta.getText(),
                        50.0,
                        via.getText(), 
                        citta.getText(), 
                        civico.getText(), 
                        cap.getText());
                
                GestioneAccesso gestione = new GestioneAccesso();
                
                if (gestione.clienteRegistrato(user)){
                    JOptionPane.showMessageDialog(this, "Registrazione completata!");
                    mostraLogin();                
                }else {
                    JOptionPane.showMessageDialog(this, "Email già registrata!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (EmailNonValidaException ex){
                JOptionPane.showMessageDialog(this, "Inserisci un'email valida (es. nome@dominio.it)", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (CapNonValidoException ex){
                JOptionPane.showMessageDialog(this, "CAP non valido ( deve avere 5 cifre )", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (CartaNonValidaException ex){
                JOptionPane.showMessageDialog(this, "Carta di credito inesistente (deve avere 16 cifre)!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NumeroCivicoNonValidoException ex){
                JOptionPane.showMessageDialog(this, "Il numero civico deve contenere almeno una cifra.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NumeroNonValidoException ex){
                JOptionPane.showMessageDialog(this, "Il numero telefonico deve contenere esattamente 10 cifre!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (UtenteGiaPresenteException ex) {
                JOptionPane.showMessageDialog(this, "Email gia registrata!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            
        });

        JButton indietro = new JButton("Torna al Login");
        indietro.addActionListener(e -> mostraLogin());

        panel.add(indietro);
        panel.add(registra);

        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Mostra la schermata di registrazione per un nuovo Ristoratore.
     * Il ristoratore deve inserire email, password, dati anagrafici e dati dell'esercizio commerciale.
     * Verifica la validità dei dati inseriti e tenta di registrare il ristoratore nel sistema.
     */
    private void mostraRegistrazioneRistoratore() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JTextField nome = new JTextField();
        JTextField cognome = new JTextField();
        JTextField nomeEsercizio = new JTextField();
        JTextField telefono = new JTextField();
        JTextField via = new JTextField();
        JTextField citta = new JTextField();
        JTextField civico = new JTextField();
        JTextField cap = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();

        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(new JLabel("Nome:")); panel.add(nome);
        panel.add(new JLabel("Cognome:")); panel.add(cognome);
        panel.add(new JLabel("Nome Esercizio:")); panel.add(nomeEsercizio);
        panel.add(new JLabel("Telefono:")); panel.add(telefono);
        panel.add(new JLabel("Via:")); panel.add(via);
        panel.add(new JLabel("Città:")); panel.add(citta);
        panel.add(new JLabel("Civico:")); panel.add(civico);
        panel.add(new JLabel("CAP:")); panel.add(cap);
        JButton registra = new JButton("Conferma Registrazione Ristoratore");
        registra.addActionListener(e -> {
            //Registra il Ristoratore
                //Verifico che tutti i campi siano "pieni"
                if (email.getText().isEmpty() || new String(password.getPassword()).isEmpty() ||
                    nome.getText().isEmpty() || cognome.getText().isEmpty() || nomeEsercizio.getText().isEmpty() ||
                    telefono.getText().isEmpty() || via.getText().isEmpty() ||
                    citta.getText().isEmpty() || civico.getText().isEmpty() || cap.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                try {
                    GestioneAccesso gestione = new GestioneAccesso();

                    EntityRistoratore user = new EntityRistoratore (email.getText(), 
                            new String(password.getPassword()),
                            nome.getText(), 
                            cognome.getText(), 
                            nomeEsercizio.getText(),
                            telefono.getText(), 
                            via.getText(), 
                            citta.getText(), 
                            civico.getText(), 
                            cap.getText());
                    
                    
                    if (gestione.ristoratoreRegistrato(user)){
                        JOptionPane.showMessageDialog(this, "Registrazione completata!");
                        mostraLogin();                
                    }else {
                        JOptionPane.showMessageDialog(this, "Email già registrata!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (EmailNonValidaException ex){
                    JOptionPane.showMessageDialog(this, "Inserisci un'email valida (es. nome@dominio.it)", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (CapNonValidoException ex){
                    JOptionPane.showMessageDialog(this, "CAP non valido ( deve avere 5 cifre )", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (NumeroCivicoNonValidoException ex){
                    JOptionPane.showMessageDialog(this, "Il numero civico deve contenere almeno una cifra.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (NumeroNonValidoException ex){
                    JOptionPane.showMessageDialog(this, "Il numero telefonico deve contenere esattamente 10 cifre!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (UtenteGiaPresenteException ex) {
                    JOptionPane.showMessageDialog(this, "Email gia registrata!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                } 
        });


        JButton indietro = new JButton("Torna al Login");
        indietro.addActionListener(e -> mostraLogin());

        panel.add(indietro);
        panel.add(registra);

        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Metodo di supporto per creare un pannello contenente una label e un componente di input (es. JTextField).
     * 
     * @param label testo della label da mostrare
     * @param field componente di input associato alla label
     * @return pannello contenente label e campo di input affiancati
     */
    private JPanel creaCampo(String etichetta, JTextField campo) {
        JPanel pannello = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(etichetta);
        campo.setPreferredSize(new Dimension(381, 55));
        pannello.add(label);
        pannello.add(campo);
        return pannello;
    }
    
}