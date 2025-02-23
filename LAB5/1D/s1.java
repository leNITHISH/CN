
import java.io.*;
import java.net.*;

public class s1 {
    private static DataInputStream din;
    private static DataOutputStream dout;
public class MatrixDecoder {
    public static class ValidationResult {
        public boolean hasError;
        public int errorRow;
        public int errorCol;
        
        public ValidationResult(boolean hasError, int errorRow, int errorCol) {
            this.hasError = hasError;
            this.errorRow = errorRow;
            this.errorCol = errorCol;
        }
    }

    public static ValidationResult validateMatrix(byte[][] matrix) {
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

    public static String decodeMatrix(byte[][] matrix) {
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
            result.append((char)charValue);
        }

        return result.toString();
    }
}    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5001);
        System.out.println("Server Listening on port 5001...");

        while (true) {
            try {
                Socket connectionSocket = serverSocket.accept();
                din = new DataInputStream(connectionSocket.getInputStream());
                dout = new DataOutputStream(connectionSocket.getOutputStream());

                new Thread(() -> {
                    try {
                        //function call here
                    } catch (IOException q) {
                        q.printStackTrace();
                    }
                }).start();
            } catch (IOException i) {
                System.err.println("Closing Connection");
                break;
            }
        }

        try {
            din.close();
            dout.close();
            serverSocket.close();
        } catch (IOException i) {
            System.out.println(i);
        }

    }
}
