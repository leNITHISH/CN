// Client.java
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address
        int port = 12345; // Server port

        try (Socket socket = new Socket(serverAddress, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            int registerNumber = 8942;
            int courseCode = 1308;
            int marksObtained = 85;
            int attendancePercentage = 92;
            int checksum = calculateChecksum(registerNumber, courseCode, marksObtained, attendancePercentage);

            courseCode = 1318; //corruption

            // Send data
            out.writeInt(registerNumber);
            out.writeInt(courseCode);
            out.writeInt(marksObtained);
            out.writeInt(attendancePercentage);

            // Calculate and send checksum
            

            out.writeInt(checksum);

            // Print sent data and checksum
            System.out.println("Data sent to server:");
            System.out.println("Register Number: " + registerNumber);
            System.out.println("Course Code: " + courseCode);
            System.out.println("Marks Obtained: " + marksObtained);
            System.out.println("Attendance Percentage: " + attendancePercentage);
            System.out.println("Checksum: " + Integer.toBinaryString(checksum));
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
