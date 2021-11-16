package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Grafica extends JFrame {
    JButton invia = new JButton("Conferma");
    Container c = new Container();
      
    JPanel home = new JPanel(new GridLayout(99, 1, 10, 5));

    JPanel chat = new JPanel();
    JLabel titolo = new JLabel();
    JTextField inserimento = new JTextField();
    Client cliente = new Client(this);// istanza client
    JTextArea textArea = new JTextArea();
    HashMap<String, ArrayList<String>> cronologie = new HashMap<String, ArrayList<String>>();
    
    JScrollPane jScrollPane = new JScrollPane(home, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public Grafica(String nome) {
        super(nome);
        c = this.getContentPane();
        chat.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(350, 500);
        cronologie.put("Globale", new ArrayList<String>());
        cronologie.get("Globale").add("Scegli il tuo username ..." + '\n');
        Bottoni bottone = new Bottoni("Globale");
        bottone.addMouseListener(new EventoMouse());
        home.add(bottone);
        // modello.addElement(regole);
        // cronologia.setModel(modello);
        // regole.setPreferredSize(new Dimension(350,30));
        // con.add(regole,BorderLayout.CENTER);

        //jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //jScrollPane.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.add(jScrollPane);
        chat("Globale");
        // c.remove(globale);
        setVisible(true);
        cliente.connetti();// connessione al server

        
        
    }

    public void chat(String nome) {
        JPanel toppanel = new JPanel();
        JButton redo = new JButton("<");
        redo.setPreferredSize(new Dimension(20, 20));
        redo.addMouseListener(new EventoMouse());
        titolo.setPreferredSize(new Dimension(300, 30));
        toppanel.add(redo, BorderLayout.NORTH);
        toppanel.add(titolo, BorderLayout.AFTER_LINE_ENDS);

        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        invia.setPreferredSize(new Dimension(80, 30));
        inserimento.setPreferredSize(new Dimension(250, 30));
        invia.addMouseListener(new EventoMouse());
        JPanel bottompanel = new JPanel();
        bottompanel.add(inserimento, BorderLayout.SOUTH);
        bottompanel.add(invia, BorderLayout.AFTER_LINE_ENDS);

        chat.add(toppanel, BorderLayout.NORTH);
        chat.add(scrollPane, BorderLayout.CENTER);
        chat.add(bottompanel, BorderLayout.SOUTH);
    }

    public void lista(String[] lista) {
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

    public void ricevere(String messaggio) {
        String[] controllo = new String[0];
        controllo=messaggio.split(" ",3);
        if(controllo[0].equals("[Globale]")){
            System.out.print(messaggio);
            cronologie.get("Globale").add(controllo[1] + " " +controllo[2]+'\n');
            if(titolo.getText().equals("Globale")){
                textArea.append(controllo[1] + " " +controllo[2]+'\n');
            }
        }
        if(controllo[0].equals("[Server]")){
            cronologie.get("Globale").add(messaggio + '\n');
            if(titolo.getText().equals("Globale")){
                textArea.append(messaggio+'\n');
            }
        }
        if(controllo[0].equals("[Lista];")){
            lista(messaggio.split(";"));
        }
        if(controllo[0].equals("[Privato]")){
            System.out.println(controllo[1]+"h"+titolo.getText()+"h");
            cronologie.get(" "+controllo[1]).add(controllo[1]+" "+controllo[2] + '\n');
            if(titolo.getText().equals(" "+controllo[1])){
                textArea.append(controllo[1] +" "+controllo[2]+'\n');
            }
        }

        if (messaggio.equals("[Server] : Scelta username completato")) {
            invia.setText("Invia");
            cliente.comunica();
        }
    }

    private class EventoMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton evento = (JButton) e.getComponent();
            switch (evento.getText()) {
            case "Conferma":
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

            case "Invia":
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

            case "<":
                textArea.setText("");
                c.removeAll();
                c.add(jScrollPane);
                revalidate();
                repaint();
                break;

            default:
                c.removeAll();
                System.out.println(evento.getText());
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
