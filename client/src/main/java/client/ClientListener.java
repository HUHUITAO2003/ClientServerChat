package client;

import java.io.*;
import java.net.Socket;

public class ClientListener implements Runnable {
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    BufferedReader inDalServer;// stream input
    Socket miosocket;
    Grafica g;

    public ClientListener(BufferedReader inDalServer,Socket miosocket, Grafica g){
        this.inDalServer = inDalServer;
        this.miosocket=miosocket;
        this.g=g;
    }

    public void run() {
        while (!miosocket.isClosed()) {//continua a ricevere messaggi fino a quando il socket non si scollega
            try {
                stringaRicevutaDalServer = inDalServer.readLine();//ascolto sull'inputstream
                g.ricevere(stringaRicevutaDalServer);//spedizione del messaggio sulla GUI
            }catch (Exception e) {
                System.out.println("Hai abbandonato la chat ciao");
                System.exit(1);
            }
        }
    }
}