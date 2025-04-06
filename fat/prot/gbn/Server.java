import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int dropAck = args.length > 0 ? Integer.parseInt(args[0]) : -1;
        ServerSocket serverSocket = new ServerSocket(6969);
        Socket socket = serverSocket.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        int expectedSeq = 0;

        while (true) {
            int seq = in.readInt();
            if (seq == -1) break;

            String word = in.readUTF();
            if (seq == expectedSeq) {
                System.out.println("[SERVER] Received packet " + seq + ": " + word);
                if (seq == dropAck) {
                    System.out.println("[SERVER] Dropped ACK for packet: " + seq);
                } else {
                    out.writeInt(seq);
                    System.out.println("[SERVER] Sent ACK for: " + seq);
                    expectedSeq++;
                }
            } else {
                System.out.println("[SERVER] Unexpected seq. Expected: " + expectedSeq + ", got: " + seq);
                out.writeInt(expectedSeq - 1); // last ACKed seq
            }
        }

        socket.close();
        serverSocket.close();
    }
}
