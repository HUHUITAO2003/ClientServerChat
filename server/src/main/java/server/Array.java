package server;

import java.util.ArrayList;

public class Array {
    ArrayList<ServerThread> a = new ArrayList<ServerThread>();

    public Array(){

    }

    public void aggiungi(ServerThread serverThread){
        a.add(serverThread);
    }

    

    public void stoppa(){
        for(int i=0; i < a.size(); i++){
            a.get(i).stop();
        }

    }
}
