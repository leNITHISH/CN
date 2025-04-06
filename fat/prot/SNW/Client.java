
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        int dropPacket = args.length > 0 ? Integer.parseInt(args[0]) : -1;
        Socket socket = new Socket("localhost", 42069);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        String[] words = "never gonna give you up".split(" ");
        int i = 0;

        while (i < words.length) {
            if (i == dropPacket) {
                System.out.println("[client] dropped packet: " + words[i]);
                dropPacket = -2; // drop only once
            } else {
                out.writeUTF(words[i]);
                System.out.println("[client] sent packet: " + words[i]);
            }

            try {
                socket.setSoTimeout(1000); // 1s timeout
                String ack = in.readUTF();
                System.out.println("[client] received ack: " + ack);
                i++; // only move to next if ack received
            } catch (SocketTimeoutException e) {
                System.out.println("[client] ack timeout. Retrying...");
            }
        }

        out.writeUTF("EOF");
        socket.close();
    }
}
