package client;

import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "127.0.0.1";// "10.22.9.3"; // indirizzo del server su cui ci vogliamo connettere
    int portaServer = 6789;// porta su cui ci vogliamo collegare
    Socket miosocket;// canale di comunicazione tra clint e sarver
    BufferedReader tastiera;// buffered per memorizzare la stringa ottenuta da tastiera
    String stringaUtente;// stringa inserita dal client
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    DataOutputStream outVersoServer;// stream output
    BufferedReader inDalServer;// stream input
    String[] nomi;

    public Socket connetti() {// metodo per connetterci al server
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));// input tastiera
            miosocket = new Socket(nomeServer, portaServer);// creazione socket con indirizzo e porta

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());// istanza per output verso al server
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));// istanza per input dal
                                                                                                // server

            do {
                System.out.println("regole per la comunicazione: " + '\n'
                        + "1:mandare immediatamente l'username(a-z,0-9 senza spazzi)" + '\n'
                        + "2:Per mandare un messaggio pubblico usa G e premere spazio,dopo averlo fatto scrvi il messaggio"
                        + '\n'
                        + "3:Per mandare un messaggio privato usa P e premere spazio,dopo averlo devi inserire il nome  utente del mittente e poi premere di nuovo spazio,dopo la pressione del secondo spazio puoi scrivere il messaggio"
                        + '\n' + "4:per lasciare la chat si usa la parola ABBBANDONA");
                System.out.println("hai capito le regole?(rispondi esclusivamente con si/no)");
                try {
                    stringaUtente = tastiera.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!stringaUtente.equals("si"));
            do {
                System.out.println("scrivi il tuo nome utente");
                stringaUtente = tastiera.readLine();// input tastiera
                outVersoServer.writeBytes(stringaUtente + '\n');// invio della stringa
                stringaRicevutaDalServer = inDalServer.readLine();
            } while (stringaRicevutaDalServer.equals("Accetato"));

            if (stringaRicevutaDalServer.indexOf("L") == 1) {
                nomi = stringaRicevutaDalServer.split(";");// serve per diveder le string dopo il carattere(split array)
                for (int i = 0; i < nomi.length; i++) {
                    System.out.println(nomi[i]);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);

        }
        return miosocket;
    }

    public void comunica() {// comunicazione con il server
        Thread listener = new Thread(new ClientListener(inDalServer, miosocket));
        Thread printer = new Thread(new ClientPrinter(outVersoServer, tastiera, miosocket));
        listener.start();
        printer.start();
    }
}
