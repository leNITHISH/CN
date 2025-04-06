
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1337);
        System.out.println("💀 Hecker server online. Waiting for victims...");

        while (true) {
            Socket client = server.accept();
            new Thread(new Handler(client)).start();
        }
    }
}

class Handler implements Runnable {
    private Socket client;

    Handler(Socket c) {
        this.client = c;
    }

    public void run() {
        try {
            String ip = client.getInetAddress().getHostAddress();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("ip leaked " + ip);
            out.close();
            client.close();
            System.out.println("📡 IP leaked: " + ip);
        } catch (IOException e) {
            System.out.println("🤕 Client error");
        }
    }
}
