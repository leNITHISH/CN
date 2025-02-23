import java.io.*;
import java.net.*;
import java.util.*;

public class q1 {
    private static Socket socket;
    private static DataInputStream dataIn;
    private static DataOutputStream dataOut;
    private static String str1 = "HELLO";

public byte[][] getBitMatrix(String text) {
    // Input validation
    if (text == null || text.isEmpty()) {
        throw new IllegalArgumentException("Input text cannot be null or empty");
    }

    int rows = 8;
    int cols = text.length();
    byte[][] matrix = new byte[rows + 1][cols + 1];

    // Convert text to binary matrix
    for (int c = 0; c < cols; c++) {
        int charValue = text.charAt(c);
        for (int r = 0; r < rows; r++) {
            matrix[r][c] = (byte) ((charValue >> (7 - r)) & 1);
        }
    }

    // Calculate row parity
    byte a;
    for (int i = 0; i < rows; i++) {
        a = 0;
        for (int j = 0; j < cols; j++) {
            a ^= matrix[i][j];
        }
        matrix[i][cols] = a;
    }

    // Calculate column parity
    for (int i = 0; i < cols + 1; i++) {
        a = 0;
        for (int j = 0; j < rows; j++) {
            a ^= matrix[j][i];
        }
        matrix[rows][i] = a;
    }

    return matrix;
}

        public static void sendMatrix(Socket socket, byte[][] matrix) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        // Write dimensions first
        dos.writeInt(matrix.length);
        dos.writeInt(matrix[0].length);
        
        // Write the matrix data
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                dos.writeByte(matrix[i][j]);
            }
        }
        dos.flush();
    }
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server waiting for connection...");
            Socket socket = serverSocket.accept();
            
            // Create and send matrix
            byte[][] matrix = getBitMatrix("Hello");
            sendMatrix(socket, matrix);
            
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}