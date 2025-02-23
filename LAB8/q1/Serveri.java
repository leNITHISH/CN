
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveri {
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";
    
    public static void main(String[] args) {
        int port = 42069;
        int expectedSeqNum = 1;  // Expect frames numbered 1..8 in order
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveri started on port " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            // GBN receiver loop
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Frame) {
                        Frame frame = (Frame) obj;
                        // Accept the frame only if it's the expected one
                        if (frame.getId() == expectedSeqNum) {
                            System.out.println(GREEN + "Received in-order Frame: " + frame + RESET);
                            expectedSeqNum++;
                        } else {
                            System.out.println(RED + "Received out-of-order Frame: " + frame + " (Expected: " + expectedSeqNum + ")" + RESET);
                        }
                        // Always send cumulative ACK (the next expected frame number)
                        Ack ack = new Ack(expectedSeqNum);
                        System.out.println(BLUE + "Sending cumulative Ack: " + ack + RESET);
                        oos.writeObject(ack);
                        oos.flush();
                    }
                } catch(Exception e) {
                    break;
                }
            }
            
            ois.close();
            oos.close();
            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
