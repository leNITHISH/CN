
import java.io.*;
import java.net.*;
import java.util.Random;

public class StopAndWaitServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4269);
        System.out.println("Server listening on port 4269...");

        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Random rand = new Random();

        int expectedFrame = 0;
        String received;
        
        while ((received = in.readLine()) != null) {
            System.out.println("Received: " + received + " (Frame " + expectedFrame + ")");
            
            // Simulate ACK loss for frame 1 (How)
            if (expectedFrame == 1 && rand.nextInt(2) == 0) { // 50% chance to lose ACK
                System.out.println("ACK " + expectedFrame + " lost! Client will resend...");
                continue;
            }

            // Send ACK
            System.out.println("Sending ACK " + expectedFrame);
            out.println("ACK " + expectedFrame);
            expectedFrame++;
        }

        socket.close();
        serverSocket.close();
    }
}
