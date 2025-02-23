import java.io.*;
import java.net.*;

public class MatrixClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            byte[][] matrix2 = getBitMatrix("HELLO"); // Send "HALLO"
            System.out.println("No error");
            printMatrix(matrix2);
            // Introduce error by flipping 'E' to 'A' (flip appropriate bit)
            matrix2[5][1] ^= 1; // This flips the bit that changes 'E' to 'A'
            System.out.println("Error");
            printMatrix(matrix2);
            sendMatrix(socket, matrix2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMatrix(byte[][] matrix) {
        for (byte[] row : matrix) {
            for (byte value : row) {
                System.out.print(value + " ");
            }
            System.out.println();  // Move to the next line after printing a row
        }
    }

    private static byte[][] getBitMatrix(String text) {
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

    private static void sendMatrix(Socket socket, byte[][] matrix) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(matrix.length);
        dos.writeInt(matrix[0].length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                dos.writeByte(matrix[i][j]);
            }
        }
        dos.flush();
    }
}
