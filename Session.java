//Session | The main class for each client that connects

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session extends Thread{

    private volatile boolean running = true;
    
    private final int id;
    private String username;
    private boolean gameOver = false;
    
    Socket socket;
    InputStreamReader isr;
    BufferedReader in;
    PrintWriter out;
    
    public Session(int id, Socket socket) throws IOException{
        this.id = id;
        this.socket = socket;
        
        isr = new InputStreamReader(socket.getInputStream());
        in = new BufferedReader(isr);
        out = new PrintWriter(socket.getOutputStream(), true);

    }
    
    @Override
    public void run() {
        
        try {
            //First message recieved from a client is it's username
            this.username = getMessage();            
            
            while (running){
                try {
                    //Read any client response
                    String clientMessage = getMessage();
                    
                    //Post a message to the Chat Room
                    sendMessage(clientMessage);
                } catch (IOException e) {
                    sendMessage(this.username + " has left the Chat Room.");
                    System.out.println(this.username + " has left the Chat Room.");
                    stopRunning();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(String message){
        for (Session clients : Server.clients){
                clients.out.println(message);
        }
    }
    
    private String getMessage() throws IOException{
        return in.readLine();
    }

    public void stopRunning()    {
        this.running = false;
    }
}