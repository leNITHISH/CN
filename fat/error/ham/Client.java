
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6969);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String msg = "Never gonna give you up";
        String encoded = encodeHamming(msg);
        out.writeUTF(encoded);
        socket.close();
    }

    // Encode each character using Hamming(12,8)
    static String encodeHamming(String msg) {
        StringBuilder encoded = new StringBuilder();
        for (char ch : msg.toCharArray()) {
            String bin = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
            encoded.append(hammingEncode(bin));
        }
        return encoded.toString();
    }

    // 8-bit data -> 12-bit Hamming code
    static String hammingEncode(String data) {
        char[] code = new char[12];
        code[2] = data.charAt(0);
        code[4] = data.charAt(1);
        code[5] = data.charAt(2);
        code[6] = data.charAt(3);
        code[8] = data.charAt(4);
        code[9] = data.charAt(5);
        code[10] = data.charAt(6);
        code[11] = data.charAt(7);

        code[0] = parity(code, new int[]{2,4,6,8,10});
        code[1] = parity(code, new int[]{2,5,6,9,10});
        code[3] = parity(code, new int[]{4,5,6,11});
        code[7] = parity(code, new int[]{8,9,10,11});

        return new String(code);
    }

    static char parity(char[] bits, int[] indices) {
        int count = 0;
        for (int i : indices)
            if (bits[i] == '1') count++;
        return (char)((count % 2) + '0');
    }
}
