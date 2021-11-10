package client;

/**
 *
 * @author juliet
 */
public class ClientMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client cliente = new Client();//istanza client
        cliente.connetti();//connessione al server
    }
    
}
