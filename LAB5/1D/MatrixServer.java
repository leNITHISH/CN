import java.io.*;
import java.net.*;

public class MatrixServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server waiting for connection...");

            while (true) {
                try {
                    Socket connectionSocket = serverSocket.accept();
                    new Thread(() -> handleClient(connectionSocket)).start();
                } catch (IOException e) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (DataInputStream din = new DataInputStream(socket.getInputStream());
             DataOutputStream dout = new DataOutputStream(socket.getOutputStream())) {

            // Receive and process first message
            System.out.println("\nReceiving first message:");
            byte[][] matrix1 = receiveMatrix(din);
            msg(matrix1);
            String originalMessage1 = decodeMatrix(matrix1);
            ValidationResult validation1 = validateMatrix(matrix1);

            // Decode the message after validation
            String decodedMessage1 = decodeMatrix(matrix1);
            System.out.println("Decoded message: " + decodedMessage1);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
private static String msg(byte[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length < 2) {
        throw new IllegalArgumentException("Invalid matrix format");
    }

    int cols = matrix[0].length - 1;  // Exclude parity column
    StringBuilder result = new StringBuilder();

    // Decode the message from the binary matrix
    for (int c = 0; c < cols; c++) {
        int charValue = 0;
        for (int r = 0; r < 8; r++) {  // Process 8 bits per character
            charValue = (charValue << 1) | matrix[r][c];
        }
        result.append((char) charValue);
    }

    // Print the decoded message
    String decodedMessage = result.toString();
    System.out.println("Decoded message: " + decodedMessage);
    
    return decodedMessage;
}

    private static byte[][] receiveMatrix(DataInputStream dis) throws IOException {
        int rows = dis.readInt();
        int cols = dis.readInt();
        byte[][] matrix = new byte[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = dis.readByte();
            }
        }
        return matrix;
    }

    private static ValidationResult validateMatrix(byte[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Invalid matrix");
        }

        int rows = matrix.length - 1;  // Excluding parity row
        int cols = matrix[0].length - 1;  // Excluding parity column

        // Check row parity
        int errorRow = -1;
        for (int i = 0; i < rows; i++) {
            byte parity = 0;
            for (int j = 0; j <= cols; j++) {  // Include parity bit in check
                parity ^= matrix[i][j];
            }
            if (parity != 0) {
                errorRow = i;
            }
        }

        // Check column parity
        int errorCol = -1;
        for (int j = 0; j <= cols; j++) {
            byte parity = 0;
            for (int i = 0; i <= rows; i++) {  // Include parity bit in check
                parity ^= matrix[i][j];
            }
            if (parity != 0) {
                errorCol = j;
            }
        }

        return new ValidationResult(errorRow != -1 || errorCol != -1, errorRow, errorCol);
    }

    private static String decodeMatrix(byte[][] matrix) {
        if (matrix == null || matrix.length != 9 || matrix[0].length < 2) {
            throw new IllegalArgumentException("Invalid matrix format");
        }

        int cols = matrix[0].length - 1;  // Exclude parity column
        StringBuilder result = new StringBuilder();

        ValidationResult validation = validateMatrix(matrix);
        if (validation.hasError) {
            // If error is detected, flip the bit at the intersection
            if (validation.errorRow >= 0 && validation.errorCol >= 0 && validation.errorCol < cols) {
                matrix[validation.errorRow][validation.errorCol] ^= 1;  // Flip the erroneous bit
                System.out.printf("Error corrected at position [%d,%d]%n",
                        validation.errorRow, validation.errorCol);
            } else {
                System.out.println("Uncorrectable error detected");
                // Continue with best effort decoding
            }
        }

        // Convert binary matrix back to text
        for (int c = 0; c < cols; c++) {
            int charValue = 0;
            for (int r = 0; r < 8; r++) {  // Process 8 bits per character
                charValue = (charValue << 1) | matrix[r][c];
            }
            result.append((char) charValue);
        }

        return result.toString();
    }

    private static class ValidationResult {
        public boolean hasError;
        public int errorRow;
        public int errorCol;

        public ValidationResult(boolean hasError, int errorRow, int errorCol) {
            this.hasError = hasError;
            this.errorRow = errorRow;
            this.errorCol = errorCol;
        }
    }
}
