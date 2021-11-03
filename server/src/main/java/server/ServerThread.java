package server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    Array a;
    ServerSocket server;// porta
    Socket client;// socket su cui ci andremo a collegare
    String stringa; // stringa ricevuta dal client
    BufferedReader inDalClient;// lettura stream dal client
    DataOutputStream outVersoClient;// output stream verso client
    String NomeClient;

    public ServerThread(Socket socket, ServerSocket serverSocket, Array a) {
        this.client = socket;
        this.server = serverSocket;
        this.a = a;
    }

    public void run() {
        try {
            connetti();
            comunica();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void connetti() throws Exception {// comunicazione con il client

        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));// lettura dello stream dal client
        outVersoClient = new DataOutputStream(client.getOutputStream());// invio dello stream verso il client
        do{
            NomeClient = inDalClient.readLine();
        }while(a.aggiungiClient(NomeClient));
        stringa = a.lista(NomeClient);
    }

    public void comunicazione(){    


    }

    public void comunica() throws Exception {// comunicazione con il client
        for (;;) {
            stringa = inDalClient.readLine();// lettura stringa proveniente dal client

            
            if (stringa.equals("FINE") || stringa.equals("STOP")) {
                outVersoClient.writeBytes(stringa + " (=> server in chiusura...)" + '\n');// invio della stringa di risposta
                System.out.println("Echo sul sever in chiusura :" + stringa);
                client.close();
                break;
            } else {

                outVersoClient.writeBytes(stringa.toUpperCase() + " (ricevuta e ritrasmessa)" + '\n');
                System.out.println("6 Echo sul server :" + stringa.toUpperCase());

            }
        }
        
        /*
        outVersoClient.close();
        inDalClient.close();
        System.out.println("9 Chiusura socket" + client);
        client.close();
        if(stringaRicevuta.equals("STOP")){
            server.close();
            a.stoppa();
        }*/
    }
}
