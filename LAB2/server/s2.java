import java.io.*;
import java.net.*;
import java.util.Scanner;
class Nithish_23BCE1230 {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private ServerSocket server = null;


    public Nithish_23BCE1230(int port){
        try{
            Scanner scanner = new Scanner(System.in);
            server = new ServerSocket(port);
            System.out.println("Server Started\nWaiting for connections...\n");
            socket=server.accept();
            System.out.println("client connected");
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
    
            String msg = "";
            while(true){
                try{
                    msg = in.readUTF();
                    if(msg.equals("end"))break;
                    System.out.println("Client: "+msg);
                    String serMsg = scanner.nextLine();
                    out.writeUTF(serMsg);
                }
                catch(IOException e){
                    System.out.println("Closing Connection ");
                    break;
                }
                
            }
            //System.out.println("Closing connection");
            socket.close();
            in.close();
            out.close();
            scanner.close();
        }       
    
        catch(IOException i){
            System.out.println("Err: "+ i);
        }
    }

}


public class s2{
    public static void main(String[] args) {
        Nithish_23BCE1230 server = new Nithish_23BCE1230(42069);
    }
}