package client;

import java.io.*;
import java.net.Socket;

public class ClientListener implements Runnable {
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    BufferedReader inDalServer;// stream input
    String[] nomi;
    Socket miosocket;

    public ClientListener(BufferedReader inDalServer,Socket miosocket){
        this.inDalServer = inDalServer;
        this.nomi=new String[0];//vettore
        this.miosocket=miosocket;
    }

    public void run() {
        while (miosocket.isConnected() == true) {
            try {
                stringaRicevutaDalServer = inDalServer.readLine();
                System.out.println(stringaRicevutaDalServer);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione con il server!2");
                System.exit(1);
            }
        }
    }
}