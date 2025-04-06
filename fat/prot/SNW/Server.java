
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int dropAck = args.length > 0 ? Integer.parseInt(args[0]) : -1;
        ServerSocket server = new ServerSocket(42069);
        System.out.println("[server] listening on port 42069");

        Socket socket = server.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        int i = 0;
        while (true) {
            String word = in.readUTF();
            if (word.equals("EOF")) break;

            System.out.println("[server] received packet: " + word);

            if (i == dropAck) {
                System.out.println("[server] dropped ack: " + word);
                dropAck = -2;
            } else {
                out.writeUTF("ACK " + word);
                System.out.println("[server] sent ack: ACK " + word);
            }
            i++;
        }

        socket.close();
        server.close();
    }
}
