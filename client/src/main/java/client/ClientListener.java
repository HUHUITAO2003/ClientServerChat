package clientserverchat;

import java.io.*;

public class ClientListener implements Runnable {
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    BufferedReader inDalServer;// stream input

    public ClientListener(BufferedReader inDalServer){
        this.inDalServer = inDalServer;
    }

    public void run() {
        for (;;) {
            try {
                stringaRicevutaDalServer = inDalServer.readLine();
                System.out.println(stringaRicevutaDalServer);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione con il server!");
                System.exit(1);
            }
        }
    }
}