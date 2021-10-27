package server;

import java.io.*;

public class ServerPrinter implements Runnable {
    DataOutputStream outVersoClient;// output stream verso client
    BufferedReader tastiera;// buffered per memorizzare la stringa ottenuta da tastiera
    String stringaModificata;// stringa di risposta

    public ServerPrinter(DataOutputStream outVersoServer)  {
        this.outVersoServer = outVersoServer;

    }

    public void run() {
        for (;;) {
            try {
                stringaUtente = tastiera.readLine();// input tastiera
                outVersoServer.writeBytes(stringaUtente + '\n');// invio della stringa
                System.out.println(stringaUtente);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione con il server!");
                System.exit(1);
            }
        }
    }
}