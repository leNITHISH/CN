
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(42069);
        byte[] buf = new byte[1024];
        System.out.println("🎤 Rick UDP Server waiting on port 42069...");

        while (true) {
            DatagramPacket request = new DatagramPacket(buf, buf.length);
            socket.receive(request);

            String msg = new String(request.getData(), 0, request.getLength()).trim();
            int n = Integer.parseInt(msg);

            String line = "Invalid line 😢";
            if (n >= 1 && n <= 44) {
                BufferedReader reader = new BufferedReader(new FileReader("../rick.txt"));
                for (int i = 1; i <= n; i++) line = reader.readLine();
                reader.close();
            }

            byte[] response = line.getBytes();
            DatagramPacket reply = new DatagramPacket(response, response.length,
                    request.getAddress(), request.getPort());
            socket.send(reply);
        }
    }
}
