package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grafica extends JFrame {
    JButton invia = new JButton("Conferma");
    Container c = new Container();
    JPanel panel = new JPanel();
    //JPanel con = new JPanel();
    JPanel bottompanel = new JPanel();
    JTextField inserimento = new JTextField();
    //DefaultListModel<JLabel> modello = new DefaultListModel<>();
    //JList<JLabel> cronologia = new JList<JLabel>();
    Client cliente = new Client(this);// istanza client
    String regole ="regole per la comunicazione: " + '\n'
    + "1:mandare immediatamente l'username(a-z,0-9 senza spazi)" + '\n'
    + "2:Per mandare un messaggio pubblico usa G e premere spazio,dopo averlo fatto scrvi il messaggio" + '\n'
    + "3:Per mandare un messaggio privato usa P e premere spazio,dopo averlo devi inserire il nome utente del mittente e poi premere di nuovo spazio,dopo la pressione del secondo spazio puoi scrivere il messaggio" + '\n' 
    + "4:per lasciare la chat si usa la parola ABBANDONA" 
    + "Scegli il tuo username ...";
    JTextArea textArea = new JTextArea();
    public Grafica(String nome) {
        super(nome);
        c = this.getContentPane();
        panel.setLayout(new BorderLayout());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(350, 500);
        
        //modello.addElement(regole);
        //cronologia.setModel(modello);
        //regole.setPreferredSize(new Dimension(350,30));
        //con.add(regole,BorderLayout.CENTER);
        textArea.setText(regole);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollPane.add(regole);
        panel.add(scrollPane, BorderLayout.CENTER);
        invia.setPreferredSize(new Dimension(80,30));
        inserimento.setPreferredSize(new Dimension(250,30));
        invia.addMouseListener(new EventoMouse());
        bottompanel.add(inserimento, BorderLayout.SOUTH);
        bottompanel.add(invia, BorderLayout.AFTER_LINE_ENDS);

        panel.add(bottompanel, BorderLayout.SOUTH);
        c.add(panel);



        setVisible(true);
        cliente.connetti();// connessione al server
    }

    public void ricevere(String messaggio){
        textArea.append(messaggio);
        if(messaggio.equals("[Server] : Scelta username completato")){
            invia.setText("poi");
        }
    }

    private class EventoMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton evento = (JButton) e.getComponent();
            switch (evento.getText()) {
                case "Conferma":
                    String nome = inserimento.getText();
                    cliente.stringaUtente=nome;
                    cliente.notify();
                break;
                case "Invia":
                    String messaggio = inserimento.getText();
                    cliente.stringaUtente=messaggio;
                    cliente.printer.notify();
                    /*String codice = c_ISBN1.getText();
                    String titolo = titolo1.getText();
                    String prezzo = prezzo1.getText();
                    String s;
                    
                    if(codice.equals("") || titolo.equals("") || prezzo.equals("") || prezzo.equals("0")){
                        s="Parametri inseriti invalidi";
                        
                    }else{    
                        Libro l = new Libro(codice, titolo, Double.valueOf(prezzo));
                        libreria.AggiungiLibro(l);
                        s="Codice libro inserito: " + codice + "\nCopie del libro presente in libreria: " + libreria.copie(codice) + "\n Libri totali inseriti: " + libreria.size();
                        
                    
                    }
                    JOptionPane.showMessageDialog(null,s);
                    break;

                case "Annulla":
                    c_ISBN1.setText("");
                    titolo1.setText("");
                    prezzo1.setText("");*/
                    break;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}

