package client;

import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "127.0.0.1";// "10.22.9.3"; // indirizzo del server su cui ci vogliamo connettere
    int portaServer = 6789;// porta su cui ci vogliamo collegare
    Socket miosocket;// canale di comunicazione tra clint e sarver
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    DataOutputStream outVersoServer;// stream output
    BufferedReader inDalServer;// stream input
    Thread listener;//thread per ricezione dei messaggi
    Grafica g;//grafica GUI

    public Client(Grafica g){
        this.g=g;
    }

    public Socket connetti() {// metodo per connetterci al server
        try {
            miosocket = new Socket(nomeServer, portaServer);// creazione socket con indirizzo e porta

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());// istanza per output verso al server
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));// istanza per input dal server

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione pop");
            System.exit(1);

        }
        return miosocket;
    }

    public void comunica() {// comunicazione con il server
        listener = new Thread(new ClientListener(inDalServer, miosocket, g));//istanza listener 
        listener.start();
    }

    public String username(String username) {// scelta username
        try {
            outVersoServer.writeBytes(username + '\n');
            stringaRicevutaDalServer = inDalServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringaRicevutaDalServer;
    }

    public void invia(String messaggio){ //invio messaggio al server
        try {
            if(messaggio.split(" ")[messaggio.split(" ").length-1].equals("ABBANDONA"))//controllo se si vuole abbandonare 
                {
                    g.ricevere("Hai abbandonato la chat"+'\n');
                    outVersoServer.writeBytes("ABBANDONA" + '\n');
                    miosocket.close();
                }else{
            outVersoServer.writeBytes(messaggio + '\n');
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
