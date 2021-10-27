package server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    Array a;
    ServerSocket server;// porta
    Socket client;// socket su cui ci andremo a collegare
    String stringaRicevuta; // stringa ricevuta dal client
    String stringaModificata;// stringa di risposta
    BufferedReader inDalClient;// lettura stream dal client
    DataOutputStream outVersoClient;// output stream verso client

    public ServerThread(Socket socket, ServerSocket serverSocket, Array a) {
        this.client = socket;
        this.server = serverSocket;
        this.a = a;
    }

    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void comunica() throws Exception {// comunicazione con il client

        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));// lettura dello stream dal client
        outVersoClient = new DataOutputStream(client.getOutputStream());// invio dello stream verso il client
        for (;;) {
            stringaRicevuta = inDalClient.readLine();// lettura stringa proveniente dal client
            if (stringaRicevuta == null || stringaRicevuta.equals("FINE") || stringaRicevuta.equals("STOP")) {
                outVersoClient.writeBytes(stringaRicevuta + " (=> server in chiusura...)" + '\n');// invio della stringa di risposta
                System.out.println("Echo sul sever in chiusura :" + stringaRicevuta);
                client.close();
                break;
            } else {

                outVersoClient.writeBytes(stringaRicevuta + " (ricevuta e ritrasmessa)" + '\n');
                System.out.println("6 Echo sul server :" + stringaRicevuta);

            }
        }

        outVersoClient.close();
        inDalClient.close();
        System.out.println("9 Chiusura socket" + client);
        client.close();
        if(stringaRicevuta.equals("STOP")){
            server.close();
            a.stoppa();
        }
    }
}
