import java.io.*;
import java.net.*;
import java.util.Scanner;

class Nithish_23BCE1230 {
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private String addr1 ="";
     Nithish_23BCE1230(String addr, int port) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            addr1 = ip.getHostAddress();
            socket = new Socket(addr, port);
            System.out.println("Connected...");
            in = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
            return;
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        //String data = addr1;
  
            try {

                out.writeUTF(addr1);
                out.flush();

                DataInputStream serverIn = new DataInputStream(socket.getInputStream());
                String serverMessage = serverIn.readUTF();
                System.out.println("Server: "+serverMessage);
            } catch (IOException i) {
                System.out.println(i);
            }
    
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

}


public class c2{
    public static void main(String[] args) {
        Nithish_23BCE1230 client = new Nithish_23BCE1230("127.0.0.1", 42069);
    }
}