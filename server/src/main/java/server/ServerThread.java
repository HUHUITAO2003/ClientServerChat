package server;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher; 

public class ServerThread extends Thread {
    ThreadMap threadMap;
    ServerSocket server;// porta
    Socket client;// socket su cui ci andremo a collegare
    String messaggio; // stringa ricevuta dal client
    BufferedReader inDalClient;// lettura stream dal client
    DataOutputStream outVersoClient;// output stream verso client
    String NomeClient;

    public ServerThread(Socket socket, ServerSocket serverSocket, ThreadMap threadMap) {
        this.client = socket;
        this.server = serverSocket;
        this.threadMap = threadMap;
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
        NomeClient = inDalClient.readLine();
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(NomeClient);
        
        while(m.find()){
            outVersoClient.writeBytes("[Server] : Username contenente simboli non accettabili, provane un'altro" + '\n');
            NomeClient = inDalClient.readLine();
            m = p.matcher(NomeClient);
        }

        while(threadMap.aggiungiClient(NomeClient,this)!=null){
            outVersoClient.writeBytes("[Server] : Username già occupato, provane un'altro" + '\n');
            NomeClient = inDalClient.readLine();
        }
        outVersoClient.writeBytes("[Server] : Scelta username completato" + '\n');
        outVersoClient.writeBytes(threadMap.lista(NomeClient) + '\n');
    }

    public void comunica() throws Exception {// comunicazione con il client
        while (messaggio!="ABBANDONA") {
            
            messaggio = inDalClient.readLine();// lettura stringa proveniente dal client
            switch(messaggio.charAt(0)){
                case 'A':
                    threadMap.rimuoviClient(NomeClient, this);
                break;

                case 'G':
                threadMap.messaggioGlobale(NomeClient, messaggio);
                break;

                case 'P':
                    String[] parti = messaggio.split(" ", 3);
                    threadMap.messaggioPrivato(NomeClient, parti[1], parti[2]);
                break;

            }
        }
        
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    public void spedisci(String messaggio){    
        try {
            outVersoClient.writeBytes(messaggio + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
