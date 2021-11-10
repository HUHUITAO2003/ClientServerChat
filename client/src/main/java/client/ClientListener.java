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
        while (!miosocket.isClosed()) {
            try {
                stringaRicevutaDalServer = inDalServer.readLine();
                if (stringaRicevutaDalServer.indexOf("L") == 1 && stringaRicevutaDalServer.indexOf("[") == 0 ) {
                    nomi = stringaRicevutaDalServer.split(";");// serve per diveder le string dopo il carattere(split array)
                    for (int i = 0; i < nomi.length; i++) {
                        System.out.println(nomi[i]);
                    }
                }else{System.out.println(stringaRicevutaDalServer);}
            }catch (Exception e) {
                System.out.println("Hai abbandonato la chat");
                System.exit(1);
            }
        }
    }
}