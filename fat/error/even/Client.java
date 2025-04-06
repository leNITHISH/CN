
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6969);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String msg = "Never gonna give you up";
        String encoded = encodeWithErrors(msg); // but make it cursed
        out.writeUTF(encoded);
        socket.close();
    }

    static String encodeWithErrors(String msg) {
        StringBuilder encoded = new StringBuilder();
        Random rand = new Random();

        for (char ch : msg.toCharArray()) {
            String bin = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
            String hamming = hammingEncodeEvenParity(bin);

            // flip a random bit (error injection 🎯)
            char[] corrupt = hamming.toCharArray();
            int errorBit = rand.nextInt(12);
            corrupt[errorBit] ^= 1; // flip bit
            encoded.append(new String(corrupt));
        }
        return encoded.toString();
    }

    static String hammingEncodeEvenParity(String data) {
        char[] code = new char[12];
        code[2] = data.charAt(0);
        code[4] = data.charAt(1);
        code[5] = data.charAt(2);
        code[6] = data.charAt(3);
        code[8] = data.charAt(4);
        code[9] = data.charAt(5);
        code[10] = data.charAt(6);
        code[11] = data.charAt(7);

        // Even parity check bits
        code[0] = parity(code, new int[]{2,4,6,8,10});
        code[1] = parity(code, new int[]{2,5,6,9,10});
        code[3] = parity(code, new int[]{4,5,6,11});
        code[7] = parity(code, new int[]{8,9,10,11});

        return new String(code);
    }

    static char parity(char[] bits, int[] idxs) {
        int ones = 0;
        for (int i : idxs)
            if (bits[i] == '1') ones++;
        return (char)((ones % 2 == 0 ? '0' : '1')); // even parity
    }
}
