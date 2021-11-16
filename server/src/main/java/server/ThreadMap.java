package server;

import java.util.HashMap;

public class ThreadMap {
    
    HashMap<String, ServerThread> threadsMap = new HashMap<String, ServerThread>();//hashmap contenente come key username, e come value un serverthread che lo gestisce

    public ThreadMap(){
    }

    public String aggiungiClient(String nome, ServerThread serverThread){//aggiunta client con controllo se username già esistente
        if(threadsMap.putIfAbsent(nome, serverThread)!=null)
        return "esistente";

        return "successo";
    }

    public void rimuoviClient(String nome, ServerThread serverThread){//rimozione client ed aggionamento della lista
        threadsMap.remove(nome, serverThread);
        messaggioGlobale(nome, "[Server] : "+nome+" ha lasciato la chat");
        aggiornamentoLista();
    }
    
    public void aggiornamentoLista(){//aggiornamento della lista dei partecipanti a tutti dopo connessione o disconnessione di un client
        String lista;
        for(String client : threadsMap.keySet()){
            lista=lista(client);
            threadsMap.get(client).spedisci(lista);
        }
    }

    public String lista(String nome){//creazione della lista dei client divisi con ;
        if(threadsMap.size()==1){
            return "[Server] : Sei l'unico connesso";
        }
        String listaclient="[Lista]; ";

        for(String i : threadsMap.keySet()){
            if(i==nome)
                continue;
            listaclient+=i+"; ";
        }
        return listaclient;
    }

    public void messaggioGlobale(String nome, String messaggio){//messaggio globale con controllo se siamo l'unico partecipante
        if(threadsMap.size()==1 && threadsMap.containsKey(nome)){
            threadsMap.get(nome).spedisci("[Server] : Sei l'unico connesso");
            return;
        }
        for(String i : threadsMap.keySet()){
            if(i==nome)
                continue;
            threadsMap.get(i).spedisci(messaggio);
        }
    }

    public void messaggioPrivato(String Mittente, String Destinatario, String messaggio){//messaggio privato con controllo dell'utente se esiste
        if(threadsMap.containsKey(Destinatario)){
        threadsMap.get(Destinatario).spedisci("[Privato] "+Mittente+" : "+messaggio);}
        else{
            threadsMap.get(Mittente).spedisci("[Server] : Utente inesisente");
        }
    }
}
