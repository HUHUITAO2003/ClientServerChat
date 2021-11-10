package server;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher; 

public class ThreadMap {
    
    HashMap<String, ServerThread> threadsMap = new HashMap<String, ServerThread>();

    public ThreadMap(){
    }

    public String aggiungiClient(String nome, ServerThread serverThread){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(nome);
        if(m.find()){
            return "simboli";
        }
        if(threadsMap.putIfAbsent(nome, serverThread)!=null)
        return "esistente";

        return "successo";
    }

    public void rimuoviClient(String nome, ServerThread serverThread){
        threadsMap.remove(nome, serverThread);
        messaggioGlobale(nome, "[Server] : "+nome+" ha lasciato la chat");
        aggiornamentoLista();
    }
    
    public void aggiornamentoLista(){
        String lista;
        for(String client : threadsMap.keySet()){
            lista=lista(client);
            threadsMap.get(client).spedisci(lista);
        }
    }

    public String lista(String nome){
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

    public void messaggioGlobale(String nome, String messaggio){
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

    public void messaggioPrivato(String Mittente, String Destinatario, String messaggio){
        if(threadsMap.containsKey(Destinatario)){
        threadsMap.get(Destinatario).spedisci("[Privato] "+Mittente+" : "+messaggio);}
        else{
            threadsMap.get(Mittente).spedisci("[Server] : Utente inesisente");
        }
    }
}
