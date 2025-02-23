
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerA {
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";

    public static void main(String[] args) {
        int port = 42069;
        // flag to drop ack for frame with id==2 only once
        boolean droppedAckForFrame2 = false;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Frame) {
                        Frame frame = (Frame) obj;
                        System.out.println(GREEN + "Received Frame: " + frame + RESET);
                        
                        if (frame.getId() == 2 && !droppedAckForFrame2) {
                            System.out.println(RED + "Simulating lost Ack for frame: " + frame + RESET);
                            droppedAckForFrame2 = true;
                            // Do not send ack for the first receipt of frame 2
                        } else {
                            Ack ack = new Ack(frame.getId() + 1);
                            System.out.println(BLUE + "Sending Ack: " + ack + RESET);
                            oos.writeObject(ack);
                            oos.flush();
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
            
            ois.close();
            oos.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
