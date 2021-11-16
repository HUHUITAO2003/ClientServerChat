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
        MultiServer tcpServer = new MultiServer();//istanza multiserver per gestione di più client
        tcpServer.start();
    }
    
}