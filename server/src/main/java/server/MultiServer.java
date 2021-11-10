package server;

import java.net.*;

/**
 *
 * @author juliet
 */


public class MultiServer{
    public void start() {
        ThreadMap threadMap = new ThreadMap();
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            for(;;){//for infinito per istanze continue dei thread per il server
                Socket socket = serverSocket.accept();//connessione tra client e server
                System.out.println("Connessione di " + socket);
                ServerThread serverThread = new ServerThread(socket, serverSocket, threadMap);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }

    }
    
}