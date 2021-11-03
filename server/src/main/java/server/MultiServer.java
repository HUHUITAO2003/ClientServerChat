package server;

import java.net.*;

/**
 *
 * @author juliet
 */


public class MultiServer{
    public void start() {
        Array a = new Array();
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            for(;;){//for infinito per istanze continue dei thread per il server
                System.out.println("1 Server in attesa ... ");
                Socket socket = serverSocket.accept();//connessione tra client e server
                System.out.println("3 Server socket " + socket);
                ServerThread serverThread = new ServerThread(socket, serverSocket, a);
                a.aggiungiServer(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }

    }
    
}