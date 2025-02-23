import java.io.*;
import java.net.*;
import java.util.Arrays;

public class CRCServer {
    static final int[] GENERATOR = {1, 0, 0, 1, 1}; // x^4 + x + 1

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

    public static boolean checkError(int[] received) {
        int[] remainder = divide(received, GENERATOR);
        for (int bit : remainder) {
            if (bit != 0) return true; // Error detected
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server waiting for connection...");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String receivedData = dis.readUTF();
        int[] receivedCodeword = Arrays.stream(receivedData.replaceAll("[\\[\\] ]", "").split(","))
                                       .mapToInt(Integer::parseInt)
                                       .toArray();
        
        if (checkError(receivedCodeword)) {
            System.out.println("Error detected in received word.");
        } else {
            System.out.println("No error detected in received word.");
        }
        
        dis.close();
        socket.close();
        serverSocket.close();
    }
}
