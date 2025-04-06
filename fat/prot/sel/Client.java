import java.io.*;
import java.net.*;
import java.util.*;

class Client {
    static int WINDOW_SIZE = 4;

    public static void main(String[] args) throws Exception {
        int drop = -1;
        if (args.length > 0) drop = Integer.parseInt(args[0]);

        Socket socket = new Socket("localhost", 42069);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        String[] words = "never gonna give you up".split(" ");
        boolean[] acked = new boolean[words.length];
        int base = 0;

        while (base < words.length) {
            for (int i = base; i < base + WINDOW_SIZE && i < words.length; i++) {
                if (!acked[i]) {
                    if (i == drop) {
                        System.out.println("❌ Dropped packet: " + i);
                        drop = -1;
                    } else {
                        out.writeInt(i);
                        out.writeUTF(words[i]);
                        System.out.println("📤 Sent packet " + i + ": " + words[i]);
                    }
                }
            }

            socket.setSoTimeout(1000);
            try {
                while (true) {
                    int ack = in.readInt();
                    System.out.println("✅ Received ACK: " + ack);
                    acked[ack] = true;
                }
            } catch (SocketTimeoutException ignored) {}

            while (base < words.length && acked[base]) base++;
        }

        socket.close();
    }
}