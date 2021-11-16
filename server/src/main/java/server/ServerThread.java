package server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    ThreadMap threadMap;//classe che gestisce la chat
    ServerSocket server;// porta
    Socket client;// socket su cui ci andremo a collegare
    String messaggio; // stringa ricevuta dal client
    BufferedReader inDalClient;// lettura stream dal client
    DataOutputStream outVersoClient;// output stream verso client
    String NomeClient; //username del client

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
            risultato=null;
            NomeClient = inDalClient.readLine();//lettura username da stream
            risultato = threadMap.aggiungiClient(NomeClient, this);//aggiunta client all'HashMap
            if(risultato.equals("esistente")){//controllo se username già esistentte
                outVersoClient.writeBytes("[Server] : Username già occupato, provane un'altro" + '\n');
            }
        }while(!risultato.equals("successo"));//si ripete finche non viene inserito uno username non esistente nell'HashMap

        outVersoClient.writeBytes("[Server] : Scelta username completato" + '\n');//notifica scelta username completata
        threadMap.aggiornamentoLista();//aggiornamento della lista dei partecipanti a tutti i partecipanti
    }

    public void comunica() throws Exception {// comunicazione con i client
        do {
            messaggio = inDalClient.readLine();// lettura messaggio proveniente dal client
            switch(messaggio.charAt(0)){//divisione tipologia messaggio
                case 'A'://Disconnesione del client
                    threadMap.rimuoviClient(NomeClient, this);
                    System.out.println("Disconessione di "+ client);
                break;

                case 'G'://messaggio globale
                    if(messaggio.split(" ")[0].equals("G") && messaggio.split(" ").length==1){
                        messaggio="G [messaggio vuoto]";//controllo di messaggio vuoto
                    }
                    threadMap.messaggioGlobale(NomeClient, "[Globale] "+NomeClient+" : "+messaggio.substring(2));
                break;

                case 'P'://messaggio privato
                    String[] parti = messaggio.split(" ", 3);
                    if(parti.length<2){//controllo di sintassi, utente mancante
                        outVersoClient.writeBytes("[Server] : Utente mancante" + '\n');
                        break;
                    }
                    if(parti.length<3){//controllo di messaggio vuoto
                        parti = new String [] {parti[0],parti[1],"[messaggio vuoto]"};
                    }
                    threadMap.messaggioPrivato(NomeClient, parti[1], parti[2]);
                break;

                default://tipologia nonriconosciuta , sintassi errata
                    outVersoClient.writeBytes("[Server] : Sintassi errata1" + '\n');
                break;
            }
        }while (!messaggio.equals("ABBANDONA"));//ripetizione fino a disconnessione 
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    public void spedisci(String messaggio){    
        try {
                outVersoClient.writeBytes(messaggio + '\n');//spedizione del messaggio verso il client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
