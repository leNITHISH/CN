import java.io.*;
import java.net.*;

public class Client {
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;

    public Client(String addr, int port) {
        try {
            socket = new Socket(addr, port);
            System.out.println("Connected...");
            in = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
            return;
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        String data = "";
        while (!data.equals("end")) {
            try {
                data = in.readLine();
                out.writeUTF(data);
                if(data.equals("end"))break;
                out.flush();

                DataInputStream serverIn = new DataInputStream(socket.getInputStream());
                String serverMessage = serverIn.readUTF();
                System.out.println(serverMessage);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        Client cli = new Client("127.0.0.1", 42069);
    }
}
