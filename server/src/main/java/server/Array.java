package server;

import java.util.ArrayList;

public class Array {
    ArrayList<ServerThread> a = new ArrayList<ServerThread>();

    public Array(){

    }

    public void aggiungiServer(ServerThread serverThread){
        a.add(serverThread);
    }

    public boolean aggiungiClient(String nome){
        for(int i=0; i < a.size(); i++){
            if(a.get(i).NomeClient.equals(nome)){
                return true;
            }
        }
        return false;
    }
    
    public String lista(String nome){
        String listaclient = null;
        for(int i=0; i < a.size(); i++){
            if(!a.get(i).NomeClient.equals(nome)){
                listaclient+=a.get(i).NomeClient+";";
            }
        }
        return listaclient;
    }

    public void stoppa(){
        for(int i=0; i < a.size(); i++){
            a.get(i).stop();
        }

    }
}
