import java.io.*;
import java.net.*;

public class HammingClient {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Get binary input from user
        System.out.print("Enter a 4-bit binary number: ");
        String dataBits = reader.readLine().trim();

        if (dataBits.length() != 4 || !dataBits.matches("[01]+")) {
            System.out.println("Invalid input. Enter exactly 4 bits (e.g., 1010).");
            return;
        }

        // Encode using Hamming (7,4)
        int[] hammingCode = encodeHamming(dataBits);

        System.out.print("Encoded Hamming Code: ");
        for (int bit : hammingCode) System.out.print(bit);
        System.out.println();

        // Flip a bit if an argument is provided
        if (args.length > 0) {
            int errorPos = Integer.parseInt(args[0]);
            if (errorPos < 1 || errorPos > 7) {
                System.out.println("Invalid bit position! Choose between 1 and 7.");
                return;
            }
            hammingCode[errorPos - 1] ^= 1; // Flip the bit
            System.out.println("Introduced error at position " + errorPos);
        }

        // Send data over network
        Socket socket = new Socket("localhost", 4269);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        for (int bit : hammingCode) out.print(bit);
        out.println(); // Send Hamming code

        socket.close();
    }

    private static int[] encodeHamming(String dataBits) {
        int[] data = new int[4]; // D1, D2, D3, D4
        int[] hamming = new int[7]; // P1, P2, D1, P3, D2, D3, D4

        for (int i = 0; i < 4; i++) data[i] = dataBits.charAt(i) - '0';

        // Assign data bits
        hamming[2] = data[0]; // D1
        hamming[4] = data[1]; // D2
        hamming[5] = data[2]; // D3
        hamming[6] = data[3]; // D4

        // Compute parity bits
        hamming[0] = hamming[2] ^ hamming[4] ^ hamming[6]; // P1
        hamming[1] = hamming[2] ^ hamming[5] ^ hamming[6]; // P2
        hamming[3] = hamming[4] ^ hamming[5] ^ hamming[6]; // P3

        return hamming;
    }
}
