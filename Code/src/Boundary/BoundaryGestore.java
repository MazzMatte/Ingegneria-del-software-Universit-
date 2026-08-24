package Boundary;

import Control.GestioneAccesso;
import Control.GestioneOrdine;

import Entity.EntityRider;

import Exception.CapNonValidoException;
import Exception.EmailNonValidaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta l'interfaccia grafica di un gestore di Pizza@Casa.
 * Consente al gestore di:
 * <ul>
 *   <li>Consultare il report mensile fornendo mese e anno.</li>
 *   <li>Inserire un nuovo rider nel sistema.</li>
 * </ul>
 */
public class BoundaryGestore extends JFrame {

    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    // Campi per l'inserimento Rider
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField capField;

    // Componenti Report
    private JTable reportTable;
    private JComboBox<Integer> comboMese;
    private JComboBox<Integer> comboAnno;

    /**
     * Costruttore della finestra del Gestore.
     * Imposta titolo, dimensioni, layout e pannelli principali.
     */
    public BoundaryGestore() {
        setTitle("Menù Gestore");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(creaMenuPrincipale(), "Menu");
        mainPanel.add(creaFormInserimentoRider(), "InserimentoRider");
        mainPanel.add(creaPanelReport(), "Report");

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");
    }

    /**
     * Crea il pannello principale con i bottoni per:
     * <ul>
     *   <li>Visualizzare report mensili</li>
     *   <li>Inserire un nuovo rider</li>
     *   <li>Uscire dall'applicazione</li>
     * </ul>
     * @return JPanel contenente il menu principale
     */
    private JPanel creaMenuPrincipale() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton btnReport = new JButton("Visualizza Report Mensili");
        btnReport.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
            
            if (model.getColumnCount() == 0) { 
                 model.addColumn("CAP");
                 model.addColumn("Tempo Medio di Consegna");
                 model.addColumn("Numero Totale Ordini");
            }
            model.setRowCount(0);

            cardLayout.show(mainPanel, "Report");
        });

        JButton btnInserisciRider = new JButton("Inserisci Rider");
        btnInserisciRider.addActionListener(e -> cardLayout.show(mainPanel, "InserimentoRider"));

        JButton btnEsci = new JButton("Esci");
        btnEsci.addActionListener(e -> System.exit(0));

        panel.add(btnReport);
        panel.add(btnInserisciRider);
        panel.add(btnEsci);

        return panel;
    }

    /**
     * Crea il form per l'inserimento di un nuovo rider con i relativi campi.
     * @return JPanel contenente il form per l'inserimento rider
     */
    private JPanel creaFormInserimentoRider() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        nomeField = new JTextField();
        cognomeField = new JTextField();
        capField = new JTextField();

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Cognome:"));
        formPanel.add(cognomeField);

        formPanel.add(new JLabel("CAP di lavoro:"));
        formPanel.add(capField);

        JButton btnAggiungi = new JButton("Aggiungi Rider");
        btnAggiungi.addActionListener(e -> aggiungiRider());

        JButton btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> {
            pulisciCampiRider();
            cardLayout.show(mainPanel, "Menu");
        });

        formPanel.add(btnIndietro);
        formPanel.add(btnAggiungi);

        panel.add(new JLabel("Inserisci Nuovo Rider", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea il pannello per la visualizzazione del report mensile,
     * con combo box per selezionare mese e anno, e tabella per visualizzare i dati.
     * @return JPanel contenente il report mensile
     */
    private JPanel creaPanelReport() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        comboMese = new JComboBox<>();
        comboAnno = new JComboBox<>();
        JButton btnCarica = new JButton("Carica Report");

        for (int i = 1; i <= 12; i++) comboMese.addItem(i);

        for (int i = 2022; i <= java.time.LocalDate.now().getYear() + 5; i++) comboAnno.addItem(i);


        topPanel.add(new JLabel("Mese:"));
        topPanel.add(comboMese);
        topPanel.add(new JLabel("Anno:"));
        topPanel.add(comboAnno);
        topPanel.add(btnCarica);

        panel.add(topPanel, BorderLayout.NORTH);

        // Tabella per il report
        reportTable = new JTable();
        
        DefaultTableModel initialModel = new DefaultTableModel();
        initialModel.addColumn("CAP");
        initialModel.addColumn("Tempo Medio di Consegna");
        initialModel.addColumn("Numero Totale Ordini");
        reportTable.setModel(initialModel); // Applica il modello iniziale

        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnIndietro);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Azione del bottone Carica Report (pulsante che effettivamente carica i dati)
        btnCarica.addActionListener(e -> caricaReport());

        return panel;
    }
    
    /**
     * Carica il report relativo al mese e anno selezionati
     * e lo visualizza nella tabella presente nel pannello report.
     * Include CAP, tempo medio di consegna e numero totale di ordini.
     * Se si verifica un errore SQL, viene mostrato un messaggio di errore.
     */
    private void caricaReport() {
        try {
            int mese = (int) comboMese.getSelectedItem();
            int anno = (int) comboAnno.getSelectedItem();

            GestioneOrdine gestoreOrdine = new GestioneOrdine(); // Rinominato per chiarezza

            List<Object[]> reportData = gestoreOrdine.ottieniReportPerMeseAnno(mese, anno); // Rinominato per chiarezza

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CAP");
            model.addColumn("Tempo Medio di Consegna");
            model.addColumn("Numero Totale Ordini"); // Nuova colonna

            if (reportData.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Nessun dato trovato per il periodo selezionato.", "Report Vuoto", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] r : reportData) {
                    String cap = (String) r[0];
                    Duration d = (Duration) r[1];
                    Integer numeroOrdini = (Integer) r[2]; // Estrazione del numero di ordini

                    String tempoStr = "N/D";
                    if (d != null && !d.isZero()) { // Controlla se la durata è valida e non zero
                        tempoStr = String.format("%02d:%02d:%02d",
                                d.toHoursPart(), d.toMinutesPart(), d.toSecondsPart());
                    }

                    model.addRow(new Object[]{cap, tempoStr, numeroOrdini});
                }
            }

            reportTable.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento del report: " + ex.getMessage(), "Errore Database", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(BoundaryGestore.class.getName()).log(Level.SEVERE, "Errore SQL caricamento report", ex);
        }
    }
   
    /**
     * Aggiunge un nuovo Rider al database.
     * Effettua i controlli sui campi obbligatori, formato email e CAP.
     * Se i dati sono validi, utilizza GestioneAccesso per inserire il nuovo rider.
     * Mostra messaggi di successo o errore all'utente.
     */
    private void aggiungiRider() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String cap = capField.getText().trim();

        // Controllo campi vuoti
        if (email.isEmpty() || password.isEmpty() || nome.isEmpty() || cognome.isEmpty() || cap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Compila tutti i campi.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GestioneAccesso gestione = new GestioneAccesso();
            
            EntityRider nuovoRider = new EntityRider(email, password, nome, cognome, cap, "libero");
            
            boolean inserito = gestione.riderRegistrato(nuovoRider);

            if (inserito) {
                JOptionPane.showMessageDialog(this, "Rider aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                pulisciCampiRider();
                cardLayout.show(mainPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Errore: Utente già esistente.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore di accesso al database.", "Errore", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (CapNonValidoException ex){
            JOptionPane.showMessageDialog(this, "CAP non valido (deve contenere 5 cifre).", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (EmailNonValidaException ex){
            JOptionPane.showMessageDialog(this, "Inserisci un'email valida (es. nome@dominio.it)", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

        
    /**
     * Pulisce tutti i campi di input del form per l'inserimento del rider.
     */
    private void pulisciCampiRider() {
        emailField.setText("");
        passwordField.setText("");
        nomeField.setText("");
        cognomeField.setText("");
        capField.setText("");
    }

}
