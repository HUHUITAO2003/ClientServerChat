package server;

import java.io.*;

public class ServerListener implements Runnable {
    String stringaRicevuta; // stringa ricevuta dal client
    BufferedReader inDalClient;// lettura stream dal client

    public ServerListener(BufferedReader inDalClient){
        this.inDalClient = inDalClient;
    }

    public void run() {
        for (;;) {
            try {
                stringaRicevuta = inDalClient.readLine();// lettura stringa proveniente dal client
                System.out.println(stringaRicevuta);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione con il server!");
                System.exit(1);
            }
        }
    }
}