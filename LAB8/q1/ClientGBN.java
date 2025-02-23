
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientGBN {
    // ANSI color codes for output
    private static final String BLUE = "\033[34m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";
    
    public static void main(String[] args) {
        // Split the sentence into 8 frames
        String[] words = {"Networks", "lab", "is", "easy", "to", "understand", "and", "implement"};
        int totalFrames = words.length; // 8 frames
        int windowSize = 4;
        String host = "localhost";
        int port = 42069;
        
        try (Socket socket = new Socket(host, port)) {
            // Set 2 sec timeout for ack reception
            socket.setSoTimeout(2000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            int base = 0;      // index of the earliest unacknowledged frame
            int nextSeqNum = 0; // next frame to send
            
            // Pre-build the frames so we can retransmit easily
            Frame[] frames = new Frame[totalFrames];
            for (int i = 0; i < totalFrames; i++) {
                frames[i] = new Frame(i, words[i]);
            }
            
            while (base < totalFrames) {
                // Send frames in the window (if not already sent)
                while (nextSeqNum < base + windowSize && nextSeqNum < totalFrames) {
                    System.out.println(BLUE + "Sending Frame: " + frames[nextSeqNum] + RESET);
                    oos.writeObject(frames[nextSeqNum]);
                    oos.flush();
                    nextSeqNum++;
                }
                
                try {
                    // Try to receive a cumulative ACK
                    Ack ack = (Ack) ois.readObject();
                    int ackNum = ack.getAck_num();
                    System.out.println(GREEN + "Received Ack: " + ack + RESET);
                    
                    // In Go-Back-N, the ACK is cumulative (expected next frame index)
                    if (ackNum > base) {
                        base = ackNum;
                    }
                } catch (SocketTimeoutException ste) {
                    // Timeout occurred: retransmit all frames in the current window
                    System.out.println(RED + "Timeout! Retransmitting frames from " + base + " to " + (nextSeqNum - 1) + RESET);
                    for (int i = base; i < nextSeqNum; i++) {
                        System.out.println(RED + "Retransmitting Frame: " + frames[i] + RESET);
                        oos.writeObject(frames[i]);
                        oos.flush();
                    }
                }
            }
            
            oos.close();
            ois.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
