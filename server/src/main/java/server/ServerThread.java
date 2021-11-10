package server;

import java.io.*;
import java.net.*;

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
        String risultato;
        do{
            NomeClient = inDalClient.readLine();
            risultato = threadMap.aggiungiClient(NomeClient, this);
            if(risultato.equals("simboli")){
                outVersoClient.writeBytes("[Server] : Username contenente simboli non accettabili, provane un'altro" + '\n');
            }
            if(risultato.equals("esistente")){
                outVersoClient.writeBytes("[Server] : Username già occupato, provane un'altro" + '\n');
            }
            
        }while(!risultato.equals("successo"));


        outVersoClient.writeBytes("[Server] : Scelta username completato" + '\n');
        threadMap.aggiornamentoLista();
    }

    public void comunica() throws Exception {// comunicazione con il client
        do {
            messaggio = inDalClient.readLine();// lettura stringa proveniente dal client
            switch(messaggio.charAt(0)){
                case 'A':
                    threadMap.rimuoviClient(NomeClient, this);
                    System.out.println("Disconessione di "+ client);
                break;

                case 'G':
                    if(messaggio.split(" ")[0].equals("G") && messaggio.split(" ").length==1){
                        messaggio="G [messaggio vuoto]";
                    }
                    threadMap.messaggioGlobale(NomeClient, "[Globale] "+NomeClient+" : "+messaggio.substring(2));
                break;

                case 'P':
                    String[] parti = messaggio.split(" ", 3);
                    if(parti.length<2){
                        outVersoClient.writeBytes("[Server] : Utente mancante" + '\n');
                        break;
                    }
                    if(parti.length<3){
                        parti = new String [] {parti[0],parti[1],"[messaggio vuoto]"};
                    }
                    threadMap.messaggioPrivato(NomeClient, parti[1], parti[2]);
                break;

                default:
                    outVersoClient.writeBytes("[Server] : Sintassi errata1" + '\n');
                break;
            }
        }while (!messaggio.equals("ABBANDONA"));
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
