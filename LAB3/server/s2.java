import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Arrays;
class Nithish_23BCE1230 {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private ServerSocket server = null;


    public Nithish_23BCE1230(int port){
        try{
            InetAddress ip = InetAddress.getLocalHost();
            String addresses[] = {
                ip.getHostAddress(),
                "127.0.0.1",
                "192.168.1.1",
                "192.168.0.101",
                "10.0.0.42",
                "203.0.113.76"
            };
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
                    String serMsg = isDer(msg, addresses);
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
    public String isDer(String ip, String ips[]){
        if(Arrays.asList(ips).contains(ip))return "Hello";
        return "Invalid";
    }

}


public class s2{
    public static void main(String[] args) {
        Nithish_23BCE1230 server = new Nithish_23BCE1230(42069);
    }
}