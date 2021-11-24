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
    JPanel home = new JPanel(new GridLayout(0, 1, -5, -5));//panello per la grafica delle home che rappresenta tutte le chat
    JPanel chat = new JPanel();//pannello per la grafica della chat
    JLabel titolo = new JLabel("Globale");//titolo della chat in cui ci troviamo
    JTextField inserimento = new JTextField();//barra di inserimento username e messaggio
    Client cliente = new Client(this);// istanza client
    JTextArea textArea = new JTextArea();
    HashMap<String, ArrayList<String>> cronologie = new HashMap<String, ArrayList<String>>();//HashMap contenente la cronologie con tutti i client
    JScrollPane j = new JScrollPane(home);
    /*JScrollPane jScrollPane = new JScrollPane(home, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);*/
    HashMap<String, JButton> listabottoni = new HashMap<String, JButton>();
    ArrayList<JButton> a = new ArrayList<JButton>();
    public Grafica(String nome) {
        super(nome);
        c = this.getContentPane();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(830, 500);
        setResizable(false);

        chat.setLayout(new BorderLayout());
        cronologie.put("Globale", new ArrayList<String>());//schermata iniziale con solo il gruppo globale
        cronologie.get("Globale").add("1. Scegli uno username senza simboli speciali" + '\n' + "2. Scrivi ABBANDONA in una chat privata o globale se vuoi chiudere la chat" + '\n' + "Scegli il tuo username ..." + '\n');
        Bottoni bottone = new Bottoni("Globale");
        bottone.addMouseListener(new EventoMouse());
        
        j.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        home.add(bottone);
        listabottoni.put("Globale",bottone);
        c.add(j);
        chat("Globale");
        for (int i = 0; i < cronologie.get("Globale").size(); i++) {
            textArea.append(cronologie.get("Globale").get(i));
        }
        for(int i =0;i<9;i++){
            Bottoni n = new Bottoni("haha");
        home.add(n);
        a.add(n);
    }
        revalidate();
        repaint();
        c.add(chat, BorderLayout.EAST);
        setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(new JPanel(), 
                    "Sei sicuro di voler abbandonare la chat?", "Abbandonare la chat?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    cliente.abbandona();
                    System.exit(0);
                }
            }
        });
        cliente.connetti();// connessione al server
    }

    public void chat(String nome) {//creazione pannello chat
        JPanel toppanel = new JPanel();//pannello superiore con tasto per tornare alla home e  il nome della chat
        //JButton redo = new JButton("<");
        //redo.setPreferredSize(new Dimension(20, 20));
        //redo.addMouseListener(new EventoMouse());
        titolo.setPreferredSize(new Dimension(500, 30));
        //toppanel.add(redo, BorderLayout.NORTH);
        toppanel.add(titolo);

        textArea.setLineWrap(true);//parte centrale con i messaggi
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        invia.setPreferredSize(new Dimension(80, 30));//parte inferiore con barra di inserimento e pulsante invio
        inserimento.setPreferredSize(new Dimension(500, 30));
        invia.addMouseListener(new EventoMouse());
        JPanel bottompanel = new JPanel();
        bottompanel.add(inserimento, BorderLayout.SOUTH);
        bottompanel.add(invia, BorderLayout.AFTER_LINE_ENDS);

        chat.add(toppanel, BorderLayout.NORTH);//aggiunta componenti al pannello chat
        chat.add(scrollPane, BorderLayout.CENTER);
        chat.add(bottompanel, BorderLayout.SOUTH);
        chat.setPreferredSize(new Dimension(600,500));
    }

    public void lista(String[] lista) {//creazione di bottone e cronologia per ogni nuovo client
        for (int i = 0; i < lista.length-1; i++) {
            if (!cronologie.containsKey(lista[i]) && !lista[i].contains("[Lista]")) {
                cronologie.put(lista[i], new ArrayList<String>());
                cronologie.get(lista[i]).add("[Server] : Ora potete iniziare a chattare" + '\n');
                Bottoni bottone = new Bottoni(lista[i]);
                bottone.addMouseListener(new EventoMouse());
                //home.add(bottone);
                ricevere("[Server] :"+lista[i]+"si è unito alla chat");
                listabottoni.put(lista[i],bottone);
            }
        }
        rinnovalista();
        revalidate();
        repaint();
    }

    public void rinnovalista(){
        home.removeAll();
        for(String i:listabottoni.keySet()){
            home.add(listabottoni.get(i));
        }
        if(listabottoni.size()<11){
            for(int i=0; i<10-listabottoni.size();i++){
                home.add(a.get(i));
            }
        }
        revalidate();
        repaint();
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
            if(messaggio.contains("lasciato la chat")){
                 cronologie.remove(" "+controllo[2].split(" ")[0]);
                 listabottoni.remove(" "+controllo[2].split(" ")[0]);
                 System.out.println(listabottoni.toString());
                rinnovalista();
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
                } else if (nome.equals("Globale")){
                    ricevere("[Server] : non puoi scegliere Globale come username");
                }else {
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
                revalidate();
                repaint();
                break;
/*
            case "<"://tasto per tornare nella home
                textArea.setText("");
                c.removeAll();
                c.add(jScrollPane);
                revalidate();
                repaint();
                break;*/

            default://tasto per scegliere in quale chat entrare
                titolo.setText(evento.getText());
                textArea.setText("");
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
