package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Grafica extends JFrame {
    JButton invia = new JButton("Scegli");//bottone per scelta username e invio messaggio
    Container c = new Container();
      
    JPanel home = new JPanel(new GridLayout(99, 1, 10, 5));//panello per la grafica delle home che rappresenta tutte le chat

    JPanel chat = new JPanel();//pannello per la grafica della chat
    JLabel titolo = new JLabel();//titolo della chat in cui ci troviamo
    JTextField inserimento = new JTextField();//barra di inserimento username e messaggio
    Client cliente = new Client(this);// istanza client
    JTextArea textArea = new JTextArea();
    HashMap<String, ArrayList<String>> cronologie = new HashMap<String, ArrayList<String>>();//HashMap contenente la cronologie con tutti i client
    
    JScrollPane jScrollPane = new JScrollPane(home, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public Grafica(String nome) {
        super(nome);
        c = this.getContentPane();
        chat.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(350, 500);
        setResizable(false);

        cronologie.put("Globale", new ArrayList<String>());//schermata iniziale con solo il gruppo globale
        cronologie.get("Globale").add("1. Scegli uno username senza simboli speciali" + '\n' + "2. Scrivi ABBANDONA in una chat privata o globale se vuoi chiudere la chat" + '\n' + "Scegli il tuo username ..." + '\n');
        Bottoni bottone = new Bottoni("Globale");

        bottone.addMouseListener(new EventoMouse());
        home.add(bottone);
        c.add(jScrollPane);
        chat("Globale");
        setVisible(true);
        cliente.connetti();// connessione al server
    }

    public void chat(String nome) {//creazione pannello chat
        JPanel toppanel = new JPanel();//pannello superiore con tasto per tornare alla home e  il nome della chat
        JButton redo = new JButton("<");
        redo.setPreferredSize(new Dimension(20, 20));
        redo.addMouseListener(new EventoMouse());
        titolo.setPreferredSize(new Dimension(300, 30));
        toppanel.add(redo, BorderLayout.NORTH);
        toppanel.add(titolo, BorderLayout.AFTER_LINE_ENDS);

        textArea.setLineWrap(true);//parte centrale con i messaggi
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        invia.setPreferredSize(new Dimension(80, 30));//parte inferiore con barra di inserimento e pulsante invio
        inserimento.setPreferredSize(new Dimension(250, 30));
        invia.addMouseListener(new EventoMouse());
        JPanel bottompanel = new JPanel();
        bottompanel.add(inserimento, BorderLayout.SOUTH);
        bottompanel.add(invia, BorderLayout.AFTER_LINE_ENDS);

        chat.add(toppanel, BorderLayout.NORTH);//aggiunta componenti al pannello chat
        chat.add(scrollPane, BorderLayout.CENTER);
        chat.add(bottompanel, BorderLayout.SOUTH);
    }

    public void lista(String[] lista) {//creazione di bottone e cronologia per ogni nuovo client
        for (int i = 0; i < lista.length-1; i++) {
            if (!cronologie.containsKey(lista[i]) && !lista[i].contains("[Lista]")) {
                cronologie.put(lista[i], new ArrayList<String>());
                cronologie.get(lista[i]).add("[Server] : Ora potete iniziare a chattare" + '\n');
                Bottoni bottone = new Bottoni(lista[i]);
                bottone.addMouseListener(new EventoMouse());
                home.add(bottone);
            }
            cronologie.get("Globale").add(lista[i] + '\n');
            if(titolo.getText().equals("Globale")){
                textArea.append(lista[i]+'\n');
            }
        }
    }

    public void ricevere(String messaggio) {//ricezione del messaggio
        String[] controllo = new String[0];
        controllo=messaggio.split(" ",3);
        if(controllo[0].equals("[Globale]")){//messaggio globale inserimento nella textlarea e nella cronologia 
            cronologie.get("Globale").add(controllo[1] + " " +controllo[2]+'\n');
            if(titolo.getText().equals("Globale")){
                textArea.append(controllo[1] + " " +controllo[2]+'\n');
            }
        }
        if(controllo[0].equals("[Server]")){//messaggi di avvertimento provenenti  dal server
            cronologie.get("Globale").add(messaggio + '\n');
            if(titolo.getText().equals("Globale")){
                textArea.append(messaggio+'\n');
            }
        }
        if(controllo[0].equals("[Lista];")){//lista dei partecipanti
            lista(messaggio.split(";"));
        }
        if(controllo[0].equals("[Privato]")){//messaggio privato inserimento nella textlarea e nella cronologia 
            cronologie.get(" "+controllo[1]).add(controllo[1]+" "+controllo[2] + '\n');
            if(titolo.getText().equals(" "+controllo[1])){
                textArea.append(controllo[1] +" "+controllo[2]+'\n');
            }
        }

        if (messaggio.equals("[Server] : Scelta username completato")) {//cambio del pulsante dopo scelta dello username 
            invia.setText("Invia");
            cliente.comunica();
        }
    }

    private class EventoMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton evento = (JButton) e.getComponent();
            switch (evento.getText()) {
            case "Scegli"://scelta dello username e controllo dei simboli speciali
                String nome = inserimento.getText();
                textArea.append("Tu : " + nome + '\n');
                cronologie.get(titolo.getText()).add("Tu : " + nome + '\n');
                inserimento.setText("");
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
                Matcher m = p.matcher(nome);
                if (nome.equals("")) {
                    ricevere("[Server] : Username vuoto" + '\n');
                } else if (!m.find()) {
                    ricevere("[Server] : Username contenente simboli non accettabili, provane un'altro" + '\n');
                } else {
                    ricevere(cliente.username(nome));
                }
                break;

            case "Invia"://invio di un messaggio globale/privato, autocompilazione sintassi
                String messaggio = inserimento.getText();
                textArea.append("Tu : " + messaggio + '\n');
                cronologie.get(titolo.getText()).add("Tu : " + messaggio + '\n');
                inserimento.setText("");
                if(titolo.getText().equals("Globale")){
                    cliente.invia("G "+messaggio);
                }else{
                    cliente.invia("P" + titolo.getText() + " "+ messaggio);
                }
                
                break;

            case "<"://tasto per tornare nella home
                textArea.setText("");
                c.removeAll();
                c.add(jScrollPane);
                revalidate();
                repaint();
                break;

            default://tasto per scegliere in quale chat entrare
                c.removeAll();
                titolo.setText(evento.getText());
                c.add(chat);
                for (int i = 0; i < cronologie.get(evento.getText()).size(); i++) {
                    textArea.append(cronologie.get(evento.getText()).get(i));
                }
                revalidate();
                repaint();
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
