/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplemultiuserchat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author tetrocs
 */
public class ChatClient {

    //GUI components

    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(30, 50);
    static JTextField textField = new JTextField(50);
    static JLabel blankLabel = new JLabel("         ");
    static JLabel blankLabel2 = new JLabel("         ");
    static JLabel nameLabel = new JLabel("          ");
    static JButton sendButton = new JButton("Send");

    //I/O stream
    static BufferedReader in;
    static PrintWriter out;

    public ChatClient() {
        //adding components
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(blankLabel2);
        chatWindow.add(textField);
        chatWindow.add(sendButton);
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(600,800);
        chatWindow.setVisible(true);
 
        textField.setEditable(false); //user shouldn't be able to enter anything until connection to server is established
        chatArea.setEditable(false); //displays messages so shouldn't be editable ever
        
        sendButton.addActionListener(new SendListener()); //adds our event listener
        textField.addActionListener(new SendListener());
    }

    void startChat() throws IOException {
        String ipAddress = JOptionPane.showInputDialog(chatWindow, "Enter IP Address", "IP Address Required!", JOptionPane.QUESTION_MESSAGE);
        Socket soc = new Socket(ipAddress, 9000);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);

        while (true) {
            String str = in.readLine(); //receives server input
            if (str.equals("NAMEREQUIRED")) {
                String name = JOptionPane.showInputDialog(chatWindow, "Enter a unique name please", "Name Required!", JOptionPane.QUESTION_MESSAGE);
                out.println(name);
            } else if (str.equals("NAMEALREADYEXISTS")) {
                String name = JOptionPane.showInputDialog(chatWindow, "Enter another name please", "The Name You Entered Is Taken!", JOptionPane.WARNING_MESSAGE);
                out.println(name);
            } else if (str.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
                nameLabel.setText("You are logged in as: " + str.substring(12));
            } else {
                chatArea.append(str + "\n");
            }
        }

    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        try {
            chat.startChat();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class SendListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatClient.out.println(ChatClient.textField.getText()); //sends message
        ChatClient.textField.setText(""); //message field resets to nothing
    }

}
