package client;

import java.io.*;
import java.net.Socket;

public class ClientPrinter implements Runnable {
    DataOutputStream outVersoServer;// stream output
    BufferedReader tastiera;// buffered per memorizzare la stringa ottenuta da tastiera
    String stringaUtente;// stringa inserita dal client
    Socket miosocket;
    
    public ClientPrinter(DataOutputStream outVersoServer,BufferedReader tastiera, Socket miosocket)  {
        this.outVersoServer = outVersoServer;
        this.tastiera=tastiera;
        this.miosocket=miosocket;
    }

    public void run() {

        while (miosocket.isConnected()==true) {
            try {
                stringaUtente = tastiera.readLine();// input tastiera
                if( stringaUtente.indexOf("G")==0 || stringaUtente.indexOf("")==1)
                {
                    //System.out.println("invio del messaggio pubblico:" + stringaUtente);
                    outVersoServer.writeBytes(stringaUtente + '\n');// invio della stringa
                }
                if( stringaUtente.indexOf("P")==0  || stringaUtente.indexOf("")==1)
                {
                    //System.out.println("invio del messaggio privato:" + stringaUtente);
                    outVersoServer.writeBytes(stringaUtente + '\n');// invio della stringa
                }

                if(stringaUtente.equals("ABBANDONA"))
                {
                   miosocket.close();
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione con il server!1");
                System.exit(1);
            }
        }
    }
}