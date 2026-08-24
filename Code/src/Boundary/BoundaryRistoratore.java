package Boundary;

import Control.GestioneOrdine;
import Control.GestioneRistorante;

import Entity.EntityRistoratore;
import Entity.EntityMenuPizza;
import Entity.EntityOrdinePizza;
import Exception.CostoNonValidoException;
import Exception.PizzaGiaPresenteException;
import Exception.PizzaNonPresenteException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Rappresenta l'interfaccia grafica per un ristoratore di Pizza@Casa.
 * <p>
 * Questa classe fornisce un JFrame con diverse schermate (gestite tramite CardLayout) che consentono
 * al ristoratore autenticato di:
 * <ul>
 * <li>Visualizzare il menu del proprio ristorante;</li>
 * <li>Aggiungere, modificare o eliminare pizze dal menu;</li>
 * <li>Visualizzare gli ordini ricevuti e da completare (con dettaglio pizze e quantità);</li>
 * <li>Uscire dal sistema.</li>
 * </ul>
 * <p>
 * Il ristoratore è identificato dall'oggetto {@link EntityRistoratore} passato nel costruttore.
 */
public class BoundaryRistoratore extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    /**
     * Riferimento all'account ristoratore loggato, utilizzato per personalizzare i dati visualizzati.
     */
    private final EntityRistoratore accountLoggato;

    /**
     * Lista delle pizze del menu del ristoratore loggato, caricata dinamicamente.
     */
    private ArrayList<EntityMenuPizza> menu;

    // Aggiungiamo riferimenti ai modelli delle liste per poterli aggiornare direttamente
    private DefaultListModel<String> eliminaPizzaListModel;
    private JList<String> eliminaPizzaList;
    private DefaultListModel<String> modificaPizzaListModel;
    private JList<String> modificaPizzaList;


    /**
     * Lista delle pizze associate agli ordini da completare del ristoratore loggato.
     */
    private ArrayList<EntityOrdinePizza> ordinePizze;


    /**
     * Costruisce l'interfaccia grafica per l'area ristoratore.
     *
     * @param accountLoggato l'utente ristoratore autenticato che accederà all'interfaccia
     */
    public BoundaryRistoratore(EntityRistoratore accountLoggato) {
        this.accountLoggato = accountLoggato;
        setTitle("Pizza@Casa - Area Ristoratore");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(creaMenuPrincipale(), "menuPrincipale");
        mainPanel.add(creaSelezioneModificaPanel(), "selezioneModifica");
        mainPanel.add(creaAggiungiPizzaPanel(), "aggiungiPizza");
        mainPanel.add(creaModificaPizzaSelectionPanel(), "modificaPizzaSelection");
        mainPanel.add(creaEliminaPizzaPanel(), "eliminaPizza"); // Aggiungiamo subito il pannello
        mainPanel.add(creaVisualizzaOrdiniPanel(), "visualizzaOrdini");

        add(mainPanel);
        cardLayout.show(mainPanel, "menuPrincipale");
        setVisible(true);
    }

    /**
     * Crea la schermata principale con le opzioni di base:
     * <ul>
     * <li>Visualizza menu</li>
     * <li>Modifica menu</li>
     * <li>Visualizza ordini</li>
     * <li>Esci dal sistema</li>
     * </ul>
     *
     * @return il JPanel con la schermata principale
     */
    private JPanel creaMenuPrincipale() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel label = new JLabel("Area Ristoratore (" + accountLoggato.getEmail() + ")", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton btnVisualizza = new JButton("Visualizza Menu");
        btnVisualizza.addActionListener(e -> {
            JPanel nuovoPanel = creaVisualizzaMenuPanel();
            mainPanel.add(nuovoPanel, "visualizzaMenu");
            cardLayout.show(mainPanel, "visualizzaMenu");
        });

        JButton btnModifica = new JButton("Modifica Menu");
        btnModifica.addActionListener(e -> cardLayout.show(mainPanel, "selezioneModifica"));

        JButton btnOrdini = new JButton("Visualizza Ordini");
        btnOrdini.addActionListener(e -> {
            JPanel nuovoPanel = creaVisualizzaOrdiniPanel();
            mainPanel.add(nuovoPanel, "visualizzaOrdini");
            cardLayout.show(mainPanel, "visualizzaOrdini");
        });


        JButton btnEsci = new JButton("Esci dal sistema");
        btnEsci.addActionListener(e -> System.exit(0));

        panel.add(label);
        panel.add(btnVisualizza);
        panel.add(btnModifica);
        panel.add(btnOrdini);
        panel.add(btnEsci);
        return panel;
    }

    /**
     * Crea la schermata per scegliere quale tipo di modifica al menu effettuare:
     * aggiunta, modifica o eliminazione di una pizza.
     *
     * @return il JPanel per la selezione del tipo di modifica
     */
    private JPanel creaSelezioneModificaPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel label = new JLabel("Modifica Menu", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton btnAggiungi = new JButton("Aggiungi Pizza");
        btnAggiungi.addActionListener(e -> cardLayout.show(mainPanel, "aggiungiPizza"));

        JButton btnModifica = new JButton("Modifica Pizza");
        btnModifica.addActionListener(e -> {
            // Aggiorna la lista prima di mostrare il pannello di selezione modifica
            aggiornaModificaPizzaList();
            cardLayout.show(mainPanel, "modificaPizzaSelection");
        });

        JButton btnElimina = new JButton("Elimina Pizza");
        btnElimina.addActionListener(e -> {
            // Aggiorna la lista prima di mostrare il pannello di eliminazione
            aggiornaEliminaPizzaList();
            cardLayout.show(mainPanel, "eliminaPizza");
        });

        JButton back = new JButton("Torna al menu principale");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipale"));

        panel.add(label);
        panel.add(btnAggiungi);
        panel.add(btnModifica);
        panel.add(btnElimina);
        panel.add(back);
        return panel;
    }


    /**
     * Crea la schermata per la visualizzazione del menu attuale del ristoratore,
     * caricando le pizze da database tramite {@link GestioneRistorante}.
     *
     * @return il JPanel per visualizzare il menu
     */
    private JPanel creaVisualizzaMenuPanel() {
        GestioneRistorante gestore = new GestioneRistorante();

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea menuArea = new JTextArea();
        menuArea.setEditable(false);
        menuArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        try {
            //Per ottenere il Menu "dinamico" del ristoratore accountLoggato
            menu = gestore.menu(accountLoggato.getEmail());
            menuArea.setText("");
            if (menu.isEmpty()) {
                menuArea.append("Il menu è vuoto. Aggiungi nuove pizze!");
            } else {
                for (EntityMenuPizza pizza : menu) {
                    menuArea.append(pizza.getNomePizza() + " - " + pizza.getDescrizione() + " - " + pizza.getCosto() + " € \n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(new JScrollPane(menuArea), BorderLayout.CENTER);

        JButton back = new JButton("Torna al menu principale");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipale"));
        panel.add(back, BorderLayout.SOUTH);

        return panel;
    }


//-----------------------------------------------------MODIFICHE AL MENU----------------------------------------------------------------

    /**
     * Crea la schermata per aggiungere una nuova pizza al menu.
     * La pizza è definita da nome, descrizione e costo.
     *
     * @return il JPanel per aggiungere una pizza
     */
    private JPanel creaAggiungiPizzaPanel() {
        JTextField nomeField = new JTextField(15);
        JTextArea descrizioneArea = new JTextArea(4, 15);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        JTextField costoField = new JTextField(15);

        JButton aggiungi = new JButton("Aggiungi Pizza");

        aggiungi.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String descrizione = descrizioneArea.getText().trim();
            String costoText = costoField.getText().trim();

            //I campi devono essere riempiti
            if (nome.isEmpty() || descrizione.isEmpty() || costoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double costo = Double.parseDouble(costoText);

                EntityMenuPizza nuovaPizza = new EntityMenuPizza(
                        accountLoggato.getEmail(), nome, descrizione, costo
                );

                // Pulizia dei campi solo se aggiunta con successo
                if (new GestioneRistorante().aggiungiPizza(nuovaPizza)) {
                    JOptionPane.showMessageDialog(this, "Pizza aggiunta al menu!");
                    nomeField.setText("");
                    descrizioneArea.setText("");
                    costoField.setText("");
                    // Aggiorna le liste di modifica ed eliminazione
                    aggiornaModificaPizzaList();
                    aggiornaEliminaPizzaList();
                } else {
                    JOptionPane.showMessageDialog(this, "Pizza già esistente.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Inserisci un costo valido (numero decimale).", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (CostoNonValidoException ex){
                JOptionPane.showMessageDialog(this, "Il costo deve essere maggiore di 0!", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (PizzaGiaPresenteException ex) {
                JOptionPane.showMessageDialog(this, "La pizza +e' gia presente nel tuo menu!", "Errore",JOptionPane.ERROR_MESSAGE);
            }
        });

        return creaFormPanel("Aggiungi Pizza", new JComponent[]{
                new JLabel("Nome Pizza:"), nomeField,
                new JLabel("Descrizione:"), new JScrollPane(descrizioneArea),
                new JLabel("Costo (€):"), costoField,
                aggiungi
        }, "selezioneModifica"); // Back to modifica selection
    }

    /**
     * Crea un pannello per selezionare una pizza da modificare dal menu.
     * Questo pannello mostra l'elenco delle pizze e consente all'utente di sceglierne una.
     *
     * @return il JPanel per selezionare una pizza da modificar
     */
    private JPanel creaModificaPizzaSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Seleziona la pizza da modificare:", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        modificaPizzaListModel = new DefaultListModel<>();
        modificaPizzaList = new JList<>(modificaPizzaListModel);
        modificaPizzaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modificaPizzaList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(modificaPizzaList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton selectPizzaButton = new JButton("Modifica Pizza Selezionata");
        selectPizzaButton.addActionListener(e -> {
            int selectedIndex = modificaPizzaList.getSelectedIndex();
            if (selectedIndex == -1 || menu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona una pizza da modificare.", "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            EntityMenuPizza pizzaDaModificare = menu.get(selectedIndex);

            if (pizzaDaModificare != null) {
                
                for (Component comp : mainPanel.getComponents()) {
                    if (comp.getName() != null && comp.getName().equals("modificaPizzaDetails")) {
                        mainPanel.remove(comp);
                        break;
                    }
                }
                JPanel modificationPanel = creaModificaPizzaPanel(pizzaDaModificare);
                mainPanel.add(modificationPanel, "modificaPizzaDetails");
                cardLayout.show(mainPanel, "modificaPizzaDetails");
            } else {
                JOptionPane.showMessageDialog(this, "Errore: Pizza non trovata nel menu.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.add(selectPizzaButton);

        JButton back = new JButton("Torna indietro");
        back.addActionListener(e -> cardLayout.show(mainPanel, "selezioneModifica"));
        southPanel.add(back);

        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Aggiorna la lista delle pizze per la modifica.
     */
    private void aggiornaModificaPizzaList() {
        GestioneRistorante gestore = new GestioneRistorante();
        modificaPizzaListModel.clear(); // Pulisce il modello esistente

        try {
            menu = gestore.menu(accountLoggato.getEmail()); // Ricarica il menu dal DB
            if (menu.isEmpty()) {
                modificaPizzaListModel.addElement("Il menu è vuoto. Nessuna pizza da poter modificare.");
                modificaPizzaList.setEnabled(false);
            } else {
                modificaPizzaList.setEnabled(true);
                for (EntityMenuPizza pizza : menu) {
                    modificaPizzaListModel.addElement(pizza.getNomePizza() + " - " + pizza.getDescrizione() + " - " + pizza.getCosto() + " €");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento del menu: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Crea la schermata per modificare una pizza esistente nel menu,
     * permettendo di aggiornare descrizione e costo tramite il nome.
     * This panel is now pre-populated with the selected pizza's data.
     *
     * @param pizzaDaModificare The EntityMenuPizza object representing the pizza to be modified.
     * @return the JPanel for modifying a pizza
     */
    private JPanel creaModificaPizzaPanel(EntityMenuPizza pizzaDaModificare) {
        
        JLabel originalNameLabel = new JLabel("Nome Pizza (non modificabile): " + pizzaDaModificare.getNomePizza());
        originalNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextArea descrizioneArea = new JTextArea(4, 15);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        descrizioneArea.setText(pizzaDaModificare.getDescrizione()); // Pre-fill description

        JTextField costoField = new JTextField(15);
        costoField.setText(String.valueOf(pizzaDaModificare.getCosto()));

        JButton modifica = new JButton("Conferma Modifica");
        modifica.addActionListener(e -> {
            String nome = pizzaDaModificare.getNomePizza();
            String nuovaDescrizione = descrizioneArea.getText().trim();
            String costoText = costoField.getText().trim();

            if (nuovaDescrizione.isEmpty() || costoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrizione e costo sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double nuovoCosto = Double.parseDouble(costoText);
                if (nuovoCosto <= 0) {
                    JOptionPane.showMessageDialog(this, "Il costo deve essere maggiore di 0!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (new GestioneRistorante().modificaPizza(accountLoggato.getEmail(), nome, nuovaDescrizione, nuovoCosto)) {
                    JOptionPane.showMessageDialog(this, "Pizza modificata con successo!");
                    // Dopo la modifica, aggiorna la lista di selezione e torna indietro
                    aggiornaModificaPizzaList();
                    aggiornaEliminaPizzaList();
                    cardLayout.show(mainPanel, "modificaPizzaSelection");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nella modifica della pizza. Assicurati che esista.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Costo non valido (deve essere un numero).", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (PizzaNonPresenteException ex) {
                JOptionPane.showMessageDialog(this, "Pizza non presente nel menu " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (CostoNonValidoException ex) {
                JOptionPane.showMessageDialog(this, "Costo non valido (numero positivo e non nullo) " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } 
        });

        return creaFormPanel("Modifica Dettagli Pizza", new JComponent[]{
                originalNameLabel,
                new JLabel("Nuova Descrizione:"), new JScrollPane(descrizioneArea),
                new JLabel("Nuovo Costo (€):"), costoField,
                modifica
        }, "modificaPizzaSelection");
    }

    /**
     * Crea la schermata per eliminare una pizza dal menu, permettendo all'utente di
     * selezionare la pizza da un elenco visualizzato.
     *
     * @return il JPanel per eliminare una pizza
     */
    private JPanel creaEliminaPizzaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Seleziona la pizza da eliminare:", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        eliminaPizzaListModel = new DefaultListModel<>();
        eliminaPizzaList = new JList<>(eliminaPizzaListModel);
        eliminaPizzaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eliminaPizzaList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(eliminaPizzaList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton elimina = new JButton("Elimina Pizza Selezionata");
        elimina.addActionListener(e -> {
            int selectedIndex = eliminaPizzaList.getSelectedIndex();
            if (selectedIndex == -1 || menu.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Seleziona una pizza da eliminare.", "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get the pizza name from the actual menu list based on the selected index
            String pizzaName = menu.get(selectedIndex).getNomePizza();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Sei sicuro di voler eliminare la pizza '" + pizzaName + "'?",
                    "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (new GestioneRistorante().eliminaPizza(accountLoggato.getEmail(), pizzaName)) {
                        JOptionPane.showMessageDialog(this, "Pizza '" + pizzaName + "' eliminata con successo!");
                        // Aggiorna direttamente i modelli delle liste dopo l'eliminazione
                        aggiornaEliminaPizzaList();
                        aggiornaModificaPizzaList(); // Aggiorna anche la lista di modifica per coerenza
                    } else {
                        JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione della pizza '" + pizzaName + "'.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.add(elimina);

        JButton back = new JButton("Torna indietro");
        back.addActionListener(e -> cardLayout.show(mainPanel, "selezioneModifica"));
        southPanel.add(back);

        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Aggiorna la lista delle pizze per l'eliminazione.
     */
    private void aggiornaEliminaPizzaList() {
        GestioneRistorante gestore = new GestioneRistorante();
        eliminaPizzaListModel.clear(); // Pulisce il modello esistente

        try {
            menu = gestore.menu(accountLoggato.getEmail()); // Ricarica il menu dal DB
            if (menu.isEmpty()) {
                eliminaPizzaListModel.addElement("Il menu è vuoto. Nessuna pizza da eliminare.");
                eliminaPizzaList.setEnabled(false);
            } else {
                eliminaPizzaList.setEnabled(true);
                for (EntityMenuPizza pizza : menu) {
                    eliminaPizzaListModel.addElement(pizza.getNomePizza() + " - " + pizza.getDescrizione() + " - " + pizza.getCosto() + " €");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento del menu: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


//---------------------------------------------VISUALIZZAZIONE DEGLI ORDINI---------------------------------------------------------------------

    /**
     * Crea la schermata per visualizzare gli ordini da completare
     * del ristoratore, mostrando codice ordine, pizze e quantità.
     *
     * @return il JPanel per visualizzare gli ordini
     */
    private JPanel creaVisualizzaOrdiniPanel() {
        GestioneOrdine gestore = new GestioneOrdine();

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ordiniArea = new JTextArea();
        ordiniArea.setEditable(false);
        ordiniArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(new JScrollPane(ordiniArea), BorderLayout.CENTER);

        int codiceTEMP = -1;    //Ordine impossibile (AUTO_INCREMENT Parte da 1)

        try {
            //Per ottenere gli ordini "dinamici" dell ristoratore accountLoggato
            ordinePizze = gestore.ordini(accountLoggato.getEmail());
            ordiniArea.setText("");

            if (ordinePizze.isEmpty()) {
                ordiniArea.append("Nessun ordine da completare al momento.");
            } else {
                for (EntityOrdinePizza pizza : ordinePizze) {
                    if (codiceTEMP != pizza.getCodiceUnivoco()) {
                        //INIZIAA LA LISTGA DI UN NUOVO ORDINE
                        ordiniArea.append("\n----------------- NUOVO ORDINE ------------------------------\n");
                        codiceTEMP = pizza.getCodiceUnivoco();
                    }
                    ordiniArea.append("- Codice Ordine = " + pizza.getCodiceUnivoco() + " | Pizza = " + pizza.getNomePizza() + " | Quantita' = " + pizza.getQuantita() + "; \n");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }

        JButton back = new JButton("Torna al menu principale");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipale"));
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }


    /**
     * Metodo di utilità per creare un pannello form con titolo, componenti dati
     * e un pulsante "Torna indietro" che riporta a una schermata specificata.
     *
     * @param titolo      il titolo della schermata
     * @param componenti  l'array di componenti Swing da aggiungere al form
     * @param backCard    il nome della card a cui tornare quando si clicca "Torna indietro"
     * @return il JPanel che contiene il form
     */
    private JPanel creaFormPanel(String titolo, JComponent[] componenti, String backCard) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel label = new JLabel(titolo, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        for (JComponent comp : componenti) form.add(comp);

        JButton back = new JButton("Torna indietro");
        back.addActionListener(e -> cardLayout.show(mainPanel, backCard));
        form.add(back);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }
}