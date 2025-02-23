import java.io.*;
import java.net.*;
import java.util.Arrays;

public class CRCClient {
    static final int[] GENERATOR = {1, 0, 0, 1, 1}; // x^4 + x + 1

    public static int[] appendZeros(int[] message, int genLength) {
        int[] padded = Arrays.copyOf(message, message.length + genLength - 1);
        return padded;
    }

    public static int[] divide(int[] dividend, int[] divisor) {
        int[] remainder = Arrays.copyOf(dividend, dividend.length);
        for (int i = 0; i <= dividend.length - divisor.length; i++) {
            if (remainder[i] == 1) {
                for (int j = 0; j < divisor.length; j++) {
                    remainder[i + j] ^= divisor[j];
                }
            }
        }
        return Arrays.copyOfRange(remainder, dividend.length - divisor.length + 1, remainder.length);
    }

    public static int[] generateCodeword(int[] message) {
        int[] dividend = appendZeros(message, GENERATOR.length);
        int[] remainder = divide(dividend, GENERATOR);
        int[] codeword = Arrays.copyOf(dividend, dividend.length);
        System.arraycopy(remainder, 0, codeword, message.length, remainder.length);
        return codeword;
    }

    public static int[] introduceError(int[] codeword) {
        codeword[codeword.length - 3] ^= 1; // Introduce error at a fixed position
        return codeword;
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        int[] message = {1, 0, 0, 1, 1, 0, 1, 1};
        int[] codeword = generateCodeword(message);
        int[] erroneousCodeword = introduceError(codeword);

        dos.writeUTF(Arrays.toString(erroneousCodeword));
        System.out.println("Sent Codeword with Error: " + Arrays.toString(erroneousCodeword));

        dos.close();
        socket.close();
    }
}
