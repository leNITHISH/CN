
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Clienti {
    private static final String BLUE = "\033[34m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";
    
    public static void main(String[] args) {
        // The message: "Networks lab is easy to understand and implement"
        String[] words = {"Networks", "lab", "is", "easy", "to", "understand", "and", "implement"};
        int totalFrames = words.length; // 8 frames
        int windowSize = 4;
        String host = "localhost";
        int port = 42069;
        
        try (Socket socket = new Socket(host, port)) {
            // Set timeout to 2 seconds for ack reception
            socket.setSoTimeout(2000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            int base = 1;       // Frames numbered 1..8
            int nextSeqNum = 1;
            // Create frames with IDs 1..8
            Frame[] frames = new Frame[totalFrames + 1];
            for (int i = 1; i <= totalFrames; i++) {
                frames[i] = new Frame(i, words[i - 1]);
            }
            
            // For simulating data frame loss: for frame 2, skip sending on first attempt.
            boolean frame2LostSimulated = false;
            
            while (base <= totalFrames) {
                // Send frames within the window
                while (nextSeqNum < base + windowSize && nextSeqNum <= totalFrames) {
                    if (nextSeqNum == 2 && !frame2LostSimulated) {
                        System.out.println(RED + "Simulating lost data frame (not sending): " + frames[nextSeqNum] + RESET);
                        frame2LostSimulated = true;
                        nextSeqNum++; // Skip sending frame 2 initially
                        continue;
                    }
                    System.out.println(BLUE + "Sending Frame: " + frames[nextSeqNum] + RESET);
                    oos.writeObject(frames[nextSeqNum]);
                    oos.flush();
                    nextSeqNum++;
                }
                
                try {
                    Ack ack = (Ack) ois.readObject();
                    int ackNum = ack.getAck_num();
                    System.out.println(GREEN + "Received Ack: " + ack + RESET);
                    // In our protocol, ACK is cumulative (next expected frame)
                    if (ackNum > base) {
                        base = ackNum;
                    }
                } catch (SocketTimeoutException ste) {
                    System.out.println(RED + "Timeout! Retransmitting frames from " + base + " to " + (nextSeqNum - 1) + RESET);
                    // Retransmit all frames in the current window
                    for (int i = base; i < nextSeqNum && i <= totalFrames; i++) {
                        System.out.println(RED + "Retransmitting Frame: " + frames[i] + RESET);
                        oos.writeObject(frames[i]);
                        oos.flush();
                    }
                    // Reset nextSeqNum to base after timeout
                    nextSeqNum = base;
                }
            }
            
            oos.close();
            ois.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
