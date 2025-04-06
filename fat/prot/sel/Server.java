import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    static int WINDOW_SIZE = 4;

    public static void main(String[] args) throws Exception {
        int drop = -1;
        if (args.length > 0) drop = Integer.parseInt(args[0]);

        ServerSocket server = new ServerSocket(42069);
        Socket socket = server.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String[] buffer = new String[100]; // enough size for this example
        boolean[] received = new boolean[100];

        while (true) {
            try {
                int seq = in.readInt();
                String word = in.readUTF();
                System.out.println("📥 Received packet " + seq + ": " + word);

                if (!received[seq]) {
                    buffer[seq] = word;
                    received[seq] = true;
                }

                if (seq == drop) {
                    System.out.println("❌ Dropped ACK: " + seq);
                    drop = -1;
                } else {
                    out.writeInt(seq);
                    System.out.println("📨 Sent ACK: " + seq);
                }
            } catch (EOFException e) {
                break;
            }
        }

        System.out.print("🧾 Final message: ");
        for (int i = 0; i < buffer.length; i++) {
            if (received[i]) System.out.print(buffer[i] + " ");
        }
        System.out.println();

        socket.close();
        server.close();
    }
}