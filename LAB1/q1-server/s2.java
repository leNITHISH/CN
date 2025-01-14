import java.io.*;
import java.net.*;

class Nithish_23BCE1230 {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Nithish_23BCE1230(int port) {
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
                    if (data.equals("end")) break;
                    System.out.println(isPrime(Integer.parseInt(data)));

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
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}

public class s2{
    public static void main(String[] args) {
        Nithish_23BCE1230 server = new Nithish_23BCE1230(42069);
    }
}