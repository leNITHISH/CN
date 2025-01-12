import java.io.*;
import java.net.*;

class Nithish_23BCE1230 {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Nithish_23BCE1230(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String data = "";
            while (!data.equals("end")) {
                try {
                    data = in.readUTF();
                    System.out.println(data);
                    if (data.equals("end")) break;
                } catch (IOException | NumberFormatException e) {
                    System.out.println("Error: " + e);
                }
            }

            System.out.println("Closing connection");
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

}

public class s1{
    public static void main(String[] args) {
        Nithish_23BCE1230 server = new Nithish_23BCE1230(42069);
    }
}