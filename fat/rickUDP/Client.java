
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter line number (1–44): ");
        String msg = scan.nextLine();
        byte[] buf = msg.getBytes();

        InetAddress addr = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, 42069);
        socket.send(packet);

        byte[] recvBuf = new byte[1024];
        DatagramPacket response = new DatagramPacket(recvBuf, recvBuf.length);
        socket.receive(response);

        String reply = new String(response.getData(), 0, response.getLength());
        System.out.println("🎶 " + reply);

        socket.close();
        scan.close();
    }
}
