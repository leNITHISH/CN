import java.io.*;
import java.net.*;

public class HammingServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4269);
        System.out.println("Server listening on port 4269...");

        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received = in.readLine();

        System.out.println("Received: " + received);

        int[] hammingCode = new int[7];
        for (int i = 0; i < 7; i++) hammingCode[i] = received.charAt(i) - '0';

        int errorPos = detectError(hammingCode);

        if (errorPos == 0) {
            System.out.println("No errors detected. Data received correctly.");
        } else {
            System.out.println("Error detected at position " + errorPos);
            hammingCode[errorPos - 1] ^= 1; // Correct error
            System.out.print("Corrected Hamming Code: ");
            for (int bit : hammingCode) System.out.print(bit);
            System.out.println();
        }

        socket.close();
        serverSocket.close();
    }

    private static int detectError(int[] hamming) {
        int p1 = hamming[0] ^ hamming[2] ^ hamming[4] ^ hamming[6];
        int p2 = hamming[1] ^ hamming[2] ^ hamming[5] ^ hamming[6];
        int p3 = hamming[3] ^ hamming[4] ^ hamming[5] ^ hamming[6];

        return (p3 * 4) + (p2 * 2) + (p1 * 1);
    }
}
