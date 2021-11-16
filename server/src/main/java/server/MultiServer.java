package server;

import java.net.*;

/**
 *
 * @author juliet
 */


public class MultiServer{
    public void start() {
        ThreadMap threadMap = new ThreadMap();//classe conttenente HashMap che memorizza e gestisce tutti i thread server
        try {
            ServerSocket serverSocket = new ServerSocket(6789);//porta su cui il server esegue il bind()
            for(;;){//for infinito per istanze continue dei serverthread per i client
                Socket socket = serverSocket.accept();//connessione tra client e server
                System.out.println("Connessione di " + socket);
                ServerThread serverThread = new ServerThread(socket, serverSocket, threadMap);//creazione istanza serverthread
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }

    }
    
}