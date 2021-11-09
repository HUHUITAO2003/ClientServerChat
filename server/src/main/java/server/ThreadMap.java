package server;

import java.util.HashMap;

public class ThreadMap {
    
    HashMap<String, ServerThread> threadsMap = new HashMap<String, ServerThread>();

    public ThreadMap(){
    }

    public ServerThread aggiungiClient(String nome, ServerThread serverThread){
        return threadsMap.putIfAbsent(nome, serverThread);
    }

    public void rimuoviClient(String nome, ServerThread serverThread){
        threadsMap.remove(nome, serverThread);
        messaggioGlobale(nome, nome+" HA LASCIATO LA CHAT");
    }
    
    public String lista(String nome){
        String listaclient="L:// ";
        for(String i : threadsMap.keySet()){
            if(i==nome)
                continue;
            listaclient+=i+"; ";
        }
        return listaclient;
    }

    public void messaggioGlobale(String nome, String messaggio){
        if(threadsMap.size()==1){
            threadsMap.get(nome).spedisci("[Server] : Sei l'unico connesso");
        }
        for(String i : threadsMap.keySet()){
            if(i==nome)
                continue;
            threadsMap.get(i).spedisci("[Globale] "+nome+" : "+messaggio);
        }
    }

    public void messaggioPrivato(String Mittente, String Destinatario, String messaggio){
        threadsMap.get(Destinatario).spedisci("[Privato] "+Mittente+" : "+messaggio);
    }
}
