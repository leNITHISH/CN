
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(6969);
        Socket client = server.accept();
        DataInputStream in = new DataInputStream(client.getInputStream());

        String encoded = in.readUTF();
        String decoded = decodeHamming(encoded);
        System.out.println("Client sent: " + decoded);
        
        in.close();
        client.close();
        server.close();
    }

    static String decodeHamming(String encoded) {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < encoded.length(); i += 12) {
            String block = encoded.substring(i, i + 12);
            msg.append((char) Integer.parseInt(correctHamming(block), 2));
        }
        return msg.toString();
    }

    static String correctHamming(String code) {
        char[] bits = code.toCharArray();
        int p1 = parity(bits, new int[]{0,2,4,6,8,10});
        int p2 = parity(bits, new int[]{1,2,5,6,9,10});
        int p3 = parity(bits, new int[]{3,4,5,6,11});
        int p4 = parity(bits, new int[]{7,8,9,10,11});

        int error = (p4 << 3) | (p3 << 2) | (p2 << 1) | p1;
        if (error != 0 && error <= 12) bits[error - 1] ^= 1;

        return "" + bits[2] + bits[4] + bits[5] + bits[6] +
                     bits[8] + bits[9] + bits[10] + bits[11];
    }

    static int parity(char[] bits, int[] indices) {
        int count = 0;
        for (int i : indices)
            if (bits[i] == '1') count++;
        return count % 2;
    }
}
