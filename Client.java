//Client for the Chat Room
//Should be started seperartly fromt the Server from an entirely different computer or directory

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {

    private PrintWriter out;
    private InputStreamReader isr;
    private BufferedReader in;
    
    private int id;
    private String username;
    
    private Socket socket;
    private final String SERVERIP = "localhost";
    private final int SERVERPORT = 1492;

    private JTextArea chatBox;
    private JButton button;
    private JScrollPane jScrollPane1;
    private JTextField textField;
    
    public Client() {
        
        try{
            //get username
            this.username = JOptionPane.showInputDialog(null, "Username: ", "Enter Username", JOptionPane.INFORMATION_MESSAGE);
            
            setUpUserInterface();
            establishConnection();
            
            //Firs thing a client does is sends its username
            sendMessage(this.username);
            
            //send message to the chat that <username> joined
            sendMessage("<" + this.username + ">" + " has joined the Chat Room.");
            
        }
        catch (Exception e){
            System.out.println(e);
        }        
    }
    
    private void run(){
        while (true){
            // Read server response
            updateChatBox(getMessage());
        }
    }

    private void setUpUserInterface(){
        jScrollPane1 = new javax.swing.JScrollPane();
        chatBox = new javax.swing.JTextArea();
        button = new javax.swing.JButton();
        textField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Room");

        chatBox.setColumns(20);
        chatBox.setRows(5);
        jScrollPane1.setViewportView(chatBox);

        button.setText("Send");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(textField))
                .addContainerGap())
        );

        pack();
        
        // Add Listeners for the button and text field
        textField.addActionListener(new ActionListener() {            
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")){
                    sendMessageWithUserName(textField.getText());
                    textField.setText("");
                }   
            }
        });
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")){
                    sendMessageWithUserName(textField.getText());
                    textField.setText("");
                }                
            }
        });
    }
    
    private void establishConnection() throws IOException{
        System.out.println("Establishing a connection with the server...");
        socket = new Socket(SERVERIP, SERVERPORT);

        out = new PrintWriter(socket.getOutputStream(), true);
        isr = new InputStreamReader(socket.getInputStream());
        in = new BufferedReader(isr);
    }
    
    private void sendMessage(String message){
        out.println(message);
    }
    
    private void sendMessageWithUserName(String message){
        out.println("<" + this.username + "> " + message);
    }
    
    private String getMessage(){
        try{
            return in.readLine();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Cannot read server message.", "Error", JOptionPane.ERROR_MESSAGE);
            return "Cannot get Server Reply";
        }
        
    }

    private void updateChatBox(String message){
        this.chatBox.append(message + "\n");
    }
    
    public String getUsrname(){
        return this.username;
    }
    
    public static void main(String[] args){
        //Set the Nimbus look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        
        Client client = new Client();
        client.setVisible(true);
        client.run();       
    }  
}
