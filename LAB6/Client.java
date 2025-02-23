import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Client <file>");
            return;
        }
        String filePath = args[0];
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }
        try (Socket socket = new Socket("localhost", 5000);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {
             
            // Read file content
            byte[] fileBytes = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileBytes);
            }
            
            // Send file name and file content to server
            dos.writeUTF(file.getName());
            dos.writeInt(fileBytes.length);
            dos.write(fileBytes);
            dos.flush();
            
            // Wait for the similarity result from server and display it
            String response = dis.readUTF();
            System.out.println("Received similarity result:\n" + response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
