
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
    // ANSI color codes
    private static final String BLUE = "\033[34m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";

    public static void main(String[] args) {
        String[] words = {"Welcome ", "to ", "Computer ", "Networks ", "lab"};
        String host = "localhost";
        int port = 42069;
        int maxRetransmissions = 3; // number of retries after initial send

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(2000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            for (int i = 0; i < words.length; i++) {
                Frame frame = new Frame(i, words[i]);
                int attempt = 0;
                boolean ackReceived = false;
                
                while (attempt <= maxRetransmissions && !ackReceived) {
                    if (attempt == 0) {
                        System.out.println(BLUE + "Sending Frame: " + frame + RESET);
                    } else {
                        System.out.println(RED + "Timeout! Retransmitting (attempt " + attempt + "): " + frame + RESET);
                    }
                    
                    oos.writeObject(frame);
                    oos.flush();
                    
                    try {
                        Ack ack = (Ack) ois.readObject();
                        // Expect ack number to be frame id + 1
                        if (ack.getAck_num() == frame.getId() + 1) {
                            System.out.println(GREEN + "Received Ack: " + ack + RESET);
                            ackReceived = true;
                        } else {
                            System.out.println(RED + "Received incorrect Ack: " + ack + RESET);
                        }
                    } catch (SocketTimeoutException ste) {
                        attempt++;
                        if (attempt > maxRetransmissions) {
                            System.out.println(RED + "Max retransmissions reached for frame: " + frame + RESET);
                        }
                    }
                }
            }
            
            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
