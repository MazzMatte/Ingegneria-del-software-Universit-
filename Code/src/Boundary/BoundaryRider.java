package Boundary;

import Control.GestioneOrdine;

import Entity.EntityCliente;
import Entity.EntityOrdine;
import Entity.EntityRider;
import Entity.EntityRistoratore;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Rappresenta l'interfaccia grafica dedicata al Rider di Pizza@Casa.
 * <p>
 * Questa classe fornisce un'area personale dove un rider può:
 * <ul>
 *   <li>Accettare un incarico di consegna del lavoro</li>
 *   <li>Visualizzare i dati dell'ordine da consegnare, comprensivi di dati del cliente e del ristoratore</li>
 * </ul>
 * <p>
 * La GUI comprende un'area di testo per la visualizzazione dei dettagli dell'ordine,
 * e tre pulsanti per aggiornare l'ordine, confermare la consegna e uscire dal sistema.
 */
public class BoundaryRider extends JFrame {
    
    /** Area di testo dove vengono mostrati i dettagli dell'ordine */
    private final JTextArea ordineArea;
    
    /** Pulsante per aggiornare i dati dell'ordine */
    private final JButton aggiornaBtn;
    
    /** Pulsante per confermare l'avvenuta consegna */
    private final JButton confermaBtn;
    
    /** Riferimento all'account rider loggato che sta utilizzando l'interfaccia */
    private final EntityRider accountLoggato;
    
    /** Riferimento all'ordine attualmente visualizzato / in corso di consegna */
    private EntityOrdine ordineCorrente = null;
    
    /** Riferimento al cliente attualmente / in corso di consegna */
    private EntityCliente cliente = null;  
    
    /** Riferimento al ristorante attualmente / in corso di consegna */
    private EntityRistoratore ristorante = null; 
    
    
    
    /**
     * Costruttore della finestra BoundaryRider.
     * Inizializza l'interfaccia grafica e associa i comportamenti ai pulsanti.
     * 
     * @param accountLoggato l'oggetto EntityRider che rappresenta il rider autenticato
     */
    public BoundaryRider(EntityRider accountLoggato) {
        
        this.accountLoggato = accountLoggato;

        setTitle("Pizza@Casa - Area Rider ");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titolo = new JLabel("Benvenuto Rider (" + accountLoggato.getEmail() + ")", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titolo, BorderLayout.NORTH);

        ordineArea = new JTextArea(10, 30);
        ordineArea.setEditable(false);
        ordineArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ordineArea.setText("Aggiornare la pagina");
        mainPanel.add(new JScrollPane(ordineArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        aggiornaBtn = new JButton("Aggiorna Ordine");
        confermaBtn = new JButton("Conferma Consegna");
        JButton esciBtn = new JButton("Esci");

        // All'avvio nessun ordine: disabilita conferma consegna
        confermaBtn.setEnabled(false);

        aggiornaBtn.addActionListener (e -> aggiorna());

        confermaBtn.addActionListener(e -> conferma());

        esciBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(aggiornaBtn);
        buttonPanel.add(confermaBtn);
        buttonPanel.add(esciBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Metodo invocato dal pulsante "Conferma Consegna".
     * Tenta di confermare l'avvenuta consegna dell'ordine corrente attraverso la classe di controllo GestioneOrdine.
     * Se l'operazione va a buon fine, aggiorna l'interfaccia disabilitando il pulsante di conferma e resettando l'area ordine.
     * Gestisce eccezioni SQL mostrando messaggi di errore all'utente.
     */
    private void conferma() {
        GestioneOrdine gestore = new GestioneOrdine();
        
        if (ordineCorrente != null) {
            try {
                boolean confermato = gestore.confermaConsegna(ordineCorrente.getCodiceUnivoco(), accountLoggato.getEmail());
                if (confermato) {
                    JOptionPane.showMessageDialog(this, "Consegna confermata!");
                    ordineArea.setText("Nessun ordine da consegnare.");
                    ordineCorrente = null;
                    aggiornaBtn.setEnabled(true);
                    confermaBtn.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante la conferma della consegna.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante la conferma: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Metodo invocato dal pulsante "Aggiorna Ordine".
     * Interroga la classe di controllo GestioneOrdine per ottenere un eventuale ordine da consegnare
     * associato al CAP di lavoro e all'email del rider.
     * Se presente, mostra i dettagli dell'ordine; altrimenti informa che non ci sono ordini disponibili.
     * Gestisce eccezioni SQL mostrando messaggi di errore all'utente.
     */
    private void aggiorna() {
        GestioneOrdine gestore = new GestioneOrdine();
        
        try {
            ordineCorrente = gestore.ottieniOrdine(accountLoggato.getCAPDiLavoro(), accountLoggato.getEmail());
            cliente = gestore.ottieniCliente(ordineCorrente.getEmailCliente());
            ristorante = gestore.ottieniRistorante(ordineCorrente.getEmailRistoratore());
            
            
            if (ordineCorrente != null) {
                mostraDettagliOrdine(ordineCorrente, cliente, ristorante);
                aggiornaBtn.setEnabled(false);
                confermaBtn.setEnabled(true);
            } else {
                ordineArea.setText("Nessun ordine disponibile per il tuo CAP ( " + accountLoggato.getCAPDiLavoro() + " ).");
                aggiornaBtn.setEnabled(true);
                confermaBtn.setEnabled(false);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento ordine: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Mostra nel JTextArea i dettagli principali dell'ordine ricevuto.
     * 
     * @param ordine l'oggetto EntityOrdine contenente i dati dell'ordine da visualizzare
     * @param cliente l'oggetto EntityCliente contenente i dati del cliente a cui consegnare l'ordine
     * @param ristorante l'oggetto EntityRistoratore contenente i dati del ristoratore da cui ritirare l'ordine
     */
    private void mostraDettagliOrdine(EntityOrdine ordine, EntityCliente cliente, EntityRistoratore ristorante) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ordine da consegnare:\n\n"); 

        // Dettagli Ordine
        sb.append("--- DETTAGLI ORDINE ---\n");
        sb.append("ID Ordine: ").append(ordine.getCodiceUnivoco()).append("\n");
        sb.append("Data: ").append(ordine.getDataOrdine()).append("\n");
        sb.append("Totale: ").append(String.format("%.2f €", ordine.getCostoTotale())).append("\n\n");

        // Dettagli Ristorante e Indirizzo di Ritiro
        sb.append("--- DAL RISTORANTE ---\n");
        sb.append("Nome Ristorante: ").append(ristorante.getNomeEsercizioCommerciale()).append("\n"); 
        sb.append("Email Ristorante: ").append(ordine.getEmailRistoratore()).append("\n");
        sb.append("Indirizzo Ritiro: ")
          .append(ristorante.getVia()) 
          .append(", ")
          .append(ristorante.getNumeroCivico()) 
          .append(" - ")
          .append(ristorante.getCAP()) 
          .append(", ")
          .append(ristorante.getCitta()) 
          .append("\n\n");

        // Dettagli Cliente e Indirizzo di Consegna
        sb.append("--- AL CLIENTE ---\n");
        sb.append("Nome Cliente: ").append(cliente.getNome()).append(" ").append(cliente.getCognome()).append("\n"); 
        sb.append("Email Cliente: ").append(ordine.getEmailCliente()).append("\n");
        sb.append("Indirizzo Consegna: ")
          .append(cliente.getVia()) 
          .append(", ")
          .append(cliente.getNumeroCivico()) 
          .append(" - ")
          .append(cliente.getCAP()) 
          .append(", ")
          .append(cliente.getCitta())
          .append("\n");

        ordineArea.setText(sb.toString());
    }
}
