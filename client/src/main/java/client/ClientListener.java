package client;

import java.io.*;
import java.net.Socket;

public class ClientListener implements Runnable {
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    BufferedReader inDalServer;// stream input
    String[] nomi;
    Socket miosocket;
    Grafica g;

    public ClientListener(BufferedReader inDalServer,Socket miosocket, Grafica g){
        this.inDalServer = inDalServer;
        this.nomi=new String[0];//vettore
        this.miosocket=miosocket;
        this.g=g;
    }

    public void run() {
        while (!miosocket.isClosed()) {
            try {
                stringaRicevutaDalServer = inDalServer.readLine();
                System.out.println(stringaRicevutaDalServer);
                if(stringaRicevutaDalServer.contains("Privato")){
                    System.out.println(stringaRicevutaDalServer);
                }
                /*if (stringaRicevutaDalServer.indexOf("L") == 1 && stringaRicevutaDalServer.indexOf("[") == 0 ) {
                    nomi = stringaRicevutaDalServer.split(";");// serve per diveder le string dopo il carattere(split array)
                    g.lista(nomi);
                }else{*/g.ricevere(stringaRicevutaDalServer);
            }catch (Exception e) {
                System.out.println(e);
                System.out.println("Hai abbandonato la chat");
                System.exit(1);
            }
        }
    }
}