
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String RESET = "\033[0m";

    public static void main(String[] args) {
        int port = 42069;
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
                        
                        Ack ack = new Ack(frame.getId() + 1);
                        System.out.println(BLUE + "Sending Ack: " + ack + RESET);
                        oos.writeObject(ack);
                        oos.flush();
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
