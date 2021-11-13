package client;

import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "127.0.0.1";// "10.22.9.3"; // indirizzo del server su cui ci vogliamo connettere
    int portaServer = 6789;// porta su cui ci vogliamo collegare
    Socket miosocket;// canale di comunicazione tra clint e sarver
    BufferedReader tastiera;// buffered per memorizzare la stringa ottenuta da tastiera
    public static String stringaUtente;// stringa inserita dal client
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    DataOutputStream outVersoServer;// stream output
    BufferedReader inDalServer;// stream input
    String[] nomi;
    Thread listener;
    Thread printer;
    Grafica g;

    public Client(Grafica g){
        this.g=g;
    }

    public Socket connetti() {// metodo per connetterci al server
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));// input tastiera
            miosocket = new Socket(nomeServer, portaServer);// creazione socket con indirizzo e porta

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());// istanza per output verso al server
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));// istanza per input dal
                                                                                                // server
/*
            do {
                System.out.println("regole per la comunicazione: " + '\n'
                        + "1:mandare immediatamente l'username(a-z,0-9 senza spazi)" + '\n'
                        + "2:Per mandare un messaggio pubblico usa G e premere spazio,dopo averlo fatto scrvi il messaggio"
                        + '\n'
                        + "3:Per mandare un messaggio privato usa P e premere spazio,dopo averlo devi inserire il nome  utente del mittente e poi premere di nuovo spazio,dopo la pressione del secondo spazio puoi scrivere il messaggio"
                        + '\n' + "4:per lasciare la chat si usa la parola ABBANDONA");
                System.out.println("hai capito le regole?(rispondi esclusivamente con si/no)");
                try {
                    stringaUtente = tastiera.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!stringaUtente.equalsIgnoreCase("si"));
*/
            do {
                g.ricevere("scrivi il tuo nome utente");
                stringaUtente = tastiera.readLine();
                wait();
                outVersoServer.writeBytes(stringaUtente + '\n');// invio della stringa
                stringaRicevutaDalServer = inDalServer.readLine();
                //g.ricevere(stringaRicevutaDalServer);
            } while (!stringaRicevutaDalServer.equals("[Server] : Scelta username completato"));

            comunica();

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione pop");
            System.exit(1);

        }
        return miosocket;
    }

    public void comunica() {// comunicazione con il server
        listener = new Thread(new ClientListener(inDalServer, miosocket));
        printer = new Thread(new ClientPrinter(outVersoServer, tastiera, miosocket));
        listener.start();
        printer.start();
    }
}
