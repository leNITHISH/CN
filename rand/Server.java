import java.io.*;
import java.net.*;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String data = "";
            while (!data.equals("end")) {
                try {
                    data = in.readUTF();
                    System.out.println(data);

                    if (data.equals("end")) break;

                    int num = Integer.parseInt(data);
                    String result = (num == 1) ? "Neither" : (isPrime(num) ? "Prime" : "Composite");
					System.out.println(result+"\n");
                    out.writeUTF(result);
                } catch (IOException | NumberFormatException e) {
                    System.out.println("Error: " + e);
                }
            }

            System.out.println("Closing connection");
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Server server = new Server(42069);
    }
}
