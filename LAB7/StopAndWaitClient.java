
import java.io.*;
import java.net.*;

public class StopAndWaitClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4269);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String[] frames = {"Hello", "How", "are", "you?"}; // Split message into frames
        int frameID = 0;

        for (String frame : frames) {
            while (true) { // Keep resending until ACK is received
                System.out.println("Sending: " + frame + " (Frame " + frameID + ")");
                out.println(frame);

                // Set timeout for ACK
                socket.setSoTimeout(2000);
                try {
                    String ack = in.readLine();
                    if (ack != null && ack.equals("ACK " + frameID)) {
                        System.out.println(ack + " received. Sending next...\n");
                        frameID++;
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("ACK timeout! Resending frame " + frameID + "...");
                }
            }
        }

        socket.close();
    }
}
