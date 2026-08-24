package Boundary;

import Control.GestioneRistorante;
import Control.GestioneOrdine;

import Entity.EntityCliente;
import Entity.EntityMenuPizza;
import Entity.EntityRistoratore;
import Exception.CreditoNonSufficienteException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta l'interfaccia grafica per il cliente di Pizza@Casa.
 * Consente di selezionare ristoranti disponibili nel proprio CAP,
 * visualizzare il menu, aggiungere pizze al carrello, rimuoverle,
 * annullare o inviare l'ordine.
 */
public class BoundaryCliente extends JFrame {

    /**
     * Account cliente loggato che utilizza l'interfaccia.
     */
    private final EntityCliente accountLoggato;

    /**
     * Gestore per interagire con i dati dei ristoranti.
     */
    private final GestioneRistorante gestioneRistorante = new GestioneRistorante();

    /**
     * ComboBox per selezionare il ristorante disponibile nel CAP.
     */
    private final JComboBox<String> comboRistoranti = new JComboBox<>();

    /**
     * ComboBox per selezionare la pizza dal menu del ristorante scelto.
     */
    private final JComboBox<String> comboMenu = new JComboBox<>();

    /**
     * Campo di testo per indicare la quantità di pizze da aggiungere al carrello.
     */
    private final JTextField quantitaField = new JTextField(5);

    /**
     * Area di testo non editabile per visualizzare la descrizione della pizza selezionata.
     */
    private final JTextArea descrizionePizzaArea = new JTextArea(3, 30);

    /**
     * Modello della lista grafica che mostra il carrello.
     */
    private final DefaultListModel<String> carrelloListModel = new DefaultListModel<>();

    /**
     * Lista grafica che visualizza le pizze aggiunte al carrello.
     */
    private final JList<String> carrelloList = new JList<>(carrelloListModel);

    /**
     * Lista degli oggetti EntityRistoratore disponibili nel CAP del cliente.
     */
    private ArrayList<EntityRistoratore> listaRistoratori;

    /**
     * Menu corrente (lista di pizze) del ristorante selezionato.
     */
    private ArrayList<EntityMenuPizza> menuCorrente;

    /**
     * Ristoratore attualmente selezionato nel ComboBox.
     */
    private EntityRistoratore ristoratoreSelezionato;

    /**
     * Carrello memorizzato come mappa nomePizza -> quantità.
     */
    private final HashMap<String, Integer> carrello = new HashMap<>();

    /**
     * Costruttore dell'interfaccia BoundaryCliente.
     *
     * @param accountLoggato l'account del cliente loggato
     * @throws SQLException in caso di errore nel caricamento dati da DB
     */
    public BoundaryCliente(EntityCliente accountLoggato) throws SQLException {
        this.accountLoggato = accountLoggato;

        this.listaRistoratori = gestioneRistorante.listaRistoranti(accountLoggato.getCAP());

        setTitle("Area Cliente - Benvenuto " + accountLoggato.getNome());
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    /**
     * Inizializza i componenti grafici dell'interfaccia.
     *
     * @throws SQLException se il caricamento dei dati da DB fallisce
     */
    private void initComponents() throws SQLException {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ---------------- Selezione dei ristoranti ----------------
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Seleziona ristorante nella tua zona (CAP: " + accountLoggato.getCAP() + "):"));

        comboRistoranti.addItem("Seleziona...");
        caricaRistoranti();
        comboRistoranti.addActionListener(e -> {
            try {
                visualizzaMenu();
            } catch (SQLException ex) {
                Logger.getLogger(BoundaryCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        topPanel.add(comboRistoranti);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Menu del Ristorante"));

        centerPanel.add(new JLabel("Pizza:"));
        centerPanel.add(comboMenu);

        centerPanel.add(new JLabel("Descrizione:"));
        descrizionePizzaArea.setWrapStyleWord(true);
        descrizionePizzaArea.setLineWrap(true);
        descrizionePizzaArea.setEditable(false);
        descrizionePizzaArea.setBackground(UIManager.getColor("Label.background"));
        descrizionePizzaArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerPanel.add(descrizionePizzaArea);

        centerPanel.add(new JLabel("Quantità:"));
        centerPanel.add(quantitaField);

        JButton btnAggiungi = new JButton("Aggiungi al carrello");
        btnAggiungi.addActionListener(e -> aggiungiPizzaAlCarrello());
        centerPanel.add(new JLabel());
        centerPanel.add(btnAggiungi);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Carrello"));

        carrelloList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(carrelloList);
        cartPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel cartButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnRimuovi = new JButton("Rimuovi dal carrello");
        btnRimuovi.addActionListener(e -> rimuoviPizzaDalCarrello());
        cartButtonPanel.add(btnRimuovi);

        JButton btnAnnulla = new JButton("Annulla ordine");
        btnAnnulla.addActionListener(e -> annullaOrdine());
        cartButtonPanel.add(btnAnnulla);

        JButton btnInviaOrdine = new JButton("Invia ordine");
        btnInviaOrdine.addActionListener(e -> inviaOrdine());
        cartButtonPanel.add(btnInviaOrdine);

        JButton btnEsci = new JButton("Esci");
        btnEsci.addActionListener(e -> System.exit(0));
        cartButtonPanel.add(btnEsci);

        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);

        panel.add(cartPanel, BorderLayout.SOUTH);

        add(panel);
    }

    /**
     * Carica i ristoranti disponibili nel CAP del cliente e popola la JComboBox relativa.
     *
     * @throws SQLException se la connessione o la query fallisce
     */
    private void caricaRistoranti() throws SQLException {
        listaRistoratori = gestioneRistorante.listaRistoranti(accountLoggato.getCAP());

        if(listaRistoratori.isEmpty()){
            comboRistoranti.addItem("nessun ristorante in zona");
            
        }else{
            for (EntityRistoratore r : listaRistoratori) {
                comboRistoranti.addItem(r.getNomeEsercizioCommerciale() + ", " + r.getRecapitoTelefonico());
            }
        }
    }

    /**
     * Visualizza il menu del ristorante selezionato aggiornando la JComboBox delle pizze
     * e la descrizione della pizza selezionata.
     *
     * @throws SQLException se il caricamento del menu dal DB fallisce
     */
    private void visualizzaMenu() throws SQLException {
        int selectedIndex = comboRistoranti.getSelectedIndex() - 1;
        comboMenu.removeAllItems();
        descrizionePizzaArea.setText("");

        if (selectedIndex >= 0 && selectedIndex < listaRistoratori.size()) {
            ristoratoreSelezionato = listaRistoratori.get(selectedIndex);
            menuCorrente = gestioneRistorante.menu(ristoratoreSelezionato.getEmail());

            comboMenu.removeActionListener(comboMenu.getActionListeners().length > 0 ? comboMenu.getActionListeners()[0] : null);

            for (EntityMenuPizza pizza : menuCorrente) {
                comboMenu.addItem(pizza.getNomePizza() + " - €" + pizza.getCosto());
            }

            comboMenu.addActionListener(e -> {
                int selectedPizzaIndex = comboMenu.getSelectedIndex();
                if (selectedPizzaIndex >= 0 && selectedPizzaIndex < menuCorrente.size()) {
                    EntityMenuPizza selectedPizza = menuCorrente.get(selectedPizzaIndex);
                    descrizionePizzaArea.setText(selectedPizza.getDescrizione());
                }
            });

            if (!menuCorrente.isEmpty()) {
                descrizionePizzaArea.setText(menuCorrente.get(0).getDescrizione());
            }
        }
    }

    /**
     * Aggiunge la pizza selezionata al carrello con la quantità indicata.
     * Effettua controlli sulla validità della quantità inserita.
     */
    private void aggiungiPizzaAlCarrello() {
        int selectedPizzaIndex = comboMenu.getSelectedIndex();
        if (selectedPizzaIndex < 0 || menuCorrente == null) return;

        String quantitaText = quantitaField.getText().trim();
        if (!quantitaText.matches("\\d+") || quantitaText.equals("0")) {
            JOptionPane.showMessageDialog(this, "Inserisci una quantità valida (numero maggiore di zero).", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantita = Integer.parseInt(quantitaText);
        EntityMenuPizza pizza = menuCorrente.get(selectedPizzaIndex);
        String nomePizza = pizza.getNomePizza();

        carrello.put(nomePizza, carrello.getOrDefault(nomePizza, 0) + quantita);

        // Disabilita la possibilità di cambiare ristorante dopo la prima aggiunta al carrello
        if (carrello.size() == 1 && carrello.get(nomePizza) == quantita) {
            comboRistoranti.setEnabled(false);
        }

        aggiornaListaCarrello();
        quantitaField.setText("");
    }

    /**
     * Aggiorna la lista grafica del carrello riflettendo il contenuto attuale della HashMap carrello.
     */
    private void aggiornaListaCarrello() {
        carrelloListModel.clear();
        for (String nomePizza : carrello.keySet()) {
            int q = carrello.get(nomePizza);
            carrelloListModel.addElement(nomePizza + " x " + q);
        }
    }

    /**
     * Rimuove la pizza selezionata dal carrello.
     * Se nessuna pizza è selezionata mostra un messaggio di errore.
     */
    private void rimuoviPizzaDalCarrello() {
        int selectedIndex = carrelloList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona una pizza dal carrello da rimuovere.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedString = carrelloListModel.getElementAt(selectedIndex);
        String nomePizza = selectedString.substring(0, selectedString.lastIndexOf(" x "));

        carrello.remove(nomePizza);

        aggiornaListaCarrello();

        // Riabilita la scelta del ristorante se il carrello è vuoto
        if (carrello.isEmpty()) {
            comboRistoranti.setEnabled(true);
        }
    }

    /**
     * Invia l'ordine al sistema di backend utilizzando GestioneOrdine.
     * Se il carrello è vuoto o l'invio fallisce mostra messaggi di errore.
     */
    private void inviaOrdine() {
        if (carrello.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il carrello è vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ArrayList<String> listaOrdine = new ArrayList<>();
            for (String nomePizza : carrello.keySet()) {
                listaOrdine.add(nomePizza + ";" + carrello.get(nomePizza));
            }

            GestioneOrdine gestore = new GestioneOrdine();
            boolean risultato = gestore.inserisciOrdine(accountLoggato.getEmail(), ristoratoreSelezionato.getEmail(), listaOrdine);

            if (risultato) {
                JOptionPane.showMessageDialog(this, "Ordine inviato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                carrello.clear();
                aggiornaListaCarrello();
            } 
        } catch (CreditoNonSufficienteException ex) {
            JOptionPane.showMessageDialog(this, "Fondi Insufficienti!", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(BoundaryCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    /**
     * Annulla l'ordine in corso, pulendo il carrello e i campi di input.
     * Chiede conferma all'utente prima di procedere.
     */
    private void annullaOrdine() {
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler annullare l'ordine?", "Conferma", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            carrello.clear();
            aggiornaListaCarrello();
            quantitaField.setText("");

            comboRistoranti.setEnabled(true);
        }
    }

}
