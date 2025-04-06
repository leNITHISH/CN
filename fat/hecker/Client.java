
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1337);
        DataInputStream in = new DataInputStream(socket.getInputStream());

        System.out.println("💀 " + in.readUTF());

        in.close();
        socket.close();
    }
}
