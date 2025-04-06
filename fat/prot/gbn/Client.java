import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String[] DATA = "never gonna give you up".split(" ");
    private static final int WINDOW_SIZE = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        int dropPacket = args.length > 0 ? Integer.parseInt(args[0]) : -1;
        Socket socket = new Socket("localhost", 6969);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int base = 0;
        int nextSeq = 0;
        int expectedAck = 0;

        while (base < DATA.length) {
            while (nextSeq < base + WINDOW_SIZE && nextSeq < DATA.length) {
                if (nextSeq == dropPacket) {
                    System.out.println("[CLIENT] Dropped packet: " + DATA[nextSeq]);
                } else {
                    out.writeInt(nextSeq);
                    out.writeUTF(DATA[nextSeq]);
                    System.out.println("[CLIENT] Sent packet " + nextSeq + ": " + DATA[nextSeq]);
                }
                nextSeq++;
            }

            try {
                socket.setSoTimeout(2000);
                expectedAck = in.readInt();
                System.out.println("[CLIENT] Received ACK for: " + expectedAck);
                base = expectedAck + 1;
            } catch (SocketTimeoutException e) {
                System.out.println("[CLIENT] Timeout. Resending from packet " + base);
                nextSeq = base;
            }
        }

        out.writeInt(-1); // signal end
        socket.close();
    }
}
