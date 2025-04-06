
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(6969);
        System.out.println("🛜 Server listening on 6969...");

        while (true) {
            Socket client = server.accept();
            System.out.println("🤝 Client connected!");

            DataInputStream in = new DataInputStream(client.getInputStream());
            String received = in.readUTF();

            System.out.println("📩 Raw data received (" + received.length() / 12 + " blocks)");

            for (int i = 0; i < received.length(); i += 12) {
                String block = received.substring(i, i + 12);
                boolean hasError = checkEvenParity(block);
                System.out.println("Block " + (i / 12 + 1) + ": " + block + " ➜ " + (hasError ? "❌ ERROR" : "✅ OK"));
            }

            client.close();
        }
    }

    static boolean checkEvenParity(String block) {
        char[] bits = block.toCharArray();
        // Recalculate parity bits based on positions
        int[][] checks = {
            {0,2,4,6,8,10},    // P1: 1st bit
            {1,2,5,6,9,10},    // P2: 2nd bit
            {3,4,5,6,11},      // P4: 4th bit
            {7,8,9,10,11}      // P8: 8th bit
        };

        for (int[] group : checks) {
            int ones = 0;
            for (int i = 1; i < group.length; i++) {
                if (bits[group[i]] == '1') ones++;
            }
            char expected = (ones % 2 == 0) ? '0' : '1';
            if (bits[group[0]] != expected)
                return true; // mismatch in parity
        }
        return false; // all parity checks passed
    }
}
