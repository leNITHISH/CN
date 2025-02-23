// Server.java
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream in = new DataInputStream(socket.getInputStream())) {

                    int registerNumber = in.readInt();
                    int courseCode = in.readInt();
                    int marksObtained = in.readInt();
                    int attendancePercentage = in.readInt();
                    int receivedChecksum = in.readInt();

                    // Print received data and checksum
                    System.out.println("Data received from client:");
                    System.out.println("Register Number: " + registerNumber);
                    System.out.println("Course Code: " + courseCode);
                    System.out.println("Marks Obtained: " + marksObtained);
                    System.out.println("Attendance Percentage: " + attendancePercentage);
                    System.out.println("Received Checksum: " + Integer.toBinaryString(receivedChecksum));

                    // Calculate checksum on the server side
                    int calculatedChecksum = calculateChecksum(registerNumber, courseCode, marksObtained, attendancePercentage);

                    // Check for data integrity
                    if (calculatedChecksum == receivedChecksum) {
                        System.out.println("Data received correctly.");
                    } else {
                        System.out.println("Data corruption detected!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateChecksum(int... values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        // One's complement
        return ~sum & 0xFFF; // Keep it to 12 bits
    }
}
