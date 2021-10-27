package server;

/**
 *
 * @author juliet
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MultiServer topServer = new MultiServer();//istanza multiserver
        topServer.start();
    }
    
}