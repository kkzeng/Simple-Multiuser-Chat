
package simplemultiuserchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tetrocs
 */
public class ChatServer {
    
    static HashMap<String, PrintWriter> hm = new HashMap<>(); //list of usernames and printwriters
    //declared as HashMap rather than Map becuase we will not be changing the type of map
    
    public static void main (String[] args) throws IOException {
        System.out.println("Waiting for clients to connect to server...");
        ServerSocket ss = new ServerSocket(9000);
        while (true) { //loop through because there are an unknown number of users
            Socket soc = ss.accept();
            System.out.println("Connection to client established");
            ConversationThread c = new ConversationThread(soc);
            c.start(); //starts a thread with new socket connection for each client
        }
    }
}

class ConversationThread extends Thread {
    Socket s;
    BufferedReader in;
    PrintWriter out;
    PrintWriter toFile;
    static FileWriter fw;
    static BufferedWriter bw;
    String name;
    
    ConversationThread(Socket s) throws IOException {
        this.s = s;
        String home = System.getProperty("user.home");
        File logs = new File(home+"/chat-logs.txt");
        logs.createNewFile();
        fw = new FileWriter(home+"/chat-logs.txt", true); //append mode
        bw = new BufferedWriter(fw);
        toFile = new PrintWriter(bw, true);
    }
    
    public void run () {
        try {
            String acceptedName;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            
            int count = 0;
            
            while(true) {
                if(count > 0) {
                    out.println("NAMEALREADYEXISTS");
                }
                else {
                    out.println("NAMEREQUIRED");
                }
                
                name = in.readLine();
                
                if(name == null) {
                    return;
                }
                else if (!ChatServer.hm.containsKey(name)) { //if name doesnt exist in our "database" 
                    acceptedName = name;
                    break;
                }
                ++count;
            }
            
            
            //successfully accepted name
            out.println("NAMEACCEPTED"+acceptedName);
            ChatServer.hm.put(acceptedName, out);
            
            while(true) {
                String message = in.readLine();
                
                if(message == null) {
                    return;
                }
                
                toFile.println(acceptedName + ": " + message); //saves to chat log
                Set keys = ChatServer.hm.keySet();
                for (Iterator i = keys.iterator(); i.hasNext();) {
                    PrintWriter w = ChatServer.hm.get((String) i.next());
                    w.println(acceptedName + ": " + message);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ConversationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}