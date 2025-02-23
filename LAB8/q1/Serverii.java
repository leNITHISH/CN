
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serverii {
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";
    
    public static void main(String[] args) {
        int port = 42069;
        int expectedSeqNum = 1; // Frames numbered 1..8
        boolean ackLostForFrame2 = false;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serverii (scenario ii) started on port " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Frame) {
                        Frame frame = (Frame) obj;
                        if (frame.getId() == expectedSeqNum) {
                            System.out.println(GREEN + "Received Frame: " + frame + RESET);
                            expectedSeqNum++;
                        } else {
                            System.out.println(RED + "Received out-of-order Frame: " + frame + " (Expected: " + expectedSeqNum + ")" + RESET);
                        }
                        
                        if (frame.getId() == 2 && !ackLostForFrame2) {
                            System.out.println(RED + "Simulating lost ACK for frame: " + frame + RESET);
                            ackLostForFrame2 = true;
                            // Do not send ACK for frame 2 this time.
                        } else {
                            Ack ack = new Ack(expectedSeqNum);
                            System.out.println(BLUE + "Sending Ack: " + ack + RESET);
                            oos.writeObject(ack);
                            oos.flush();
                        }
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
