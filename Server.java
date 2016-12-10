
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//Server for the Chat Room | Can accept any amount of client connections
//This server constantly waits for a new client to connect.             
//Once a new client connects, it starts a Client Session as a new thread.
public class Server {
    
    ServerSocket listener;
    Socket socket;
    InputStreamReader isr;
    BufferedReader in;
    PrintWriter out;
    
    public static ArrayList<Session> clients = new ArrayList<>();
    private final int PORT = 1492;
    private int clientNumber = -1;    
    public boolean gameOver = false;
    
    public Server() throws IOException{

        listener = new ServerSocket(PORT);
        System.out.println("Server sucessfully started on port " + this.PORT);

        while (!gameOver) {
            try {
                // Wait for next client connection
                socket = listener.accept();
                System.out.println("New client connected and assigned the ID: " + (++clientNumber));

                Session sesh = new Session(clientNumber, socket);                
                clients.add(sesh);
                sesh.start();                

            } catch (Exception e){
                System.out.println("Server Error: " + e);
                this.gameOver = true;
                closeServerSocket();
                sendGameOverSignal();
            }
        }  
    }
    
    private void closeServerSocket() throws IOException{
        listener.close();
        System.out.println("Closed the Server Socket.");
    }

    private void sendGameOverSignal(){
        System.out.println("Closing all the clients");
        for (Session client : clients){
            client.stopRunning();
        }
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception e){
            System.out.println(e);
        }
    }    
}