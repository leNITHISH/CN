
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        boolean showBinary = false;
        for (String arg : args) if (arg.equals("-b")) showBinary = true;

        ServerSocket server = new ServerSocket(6969);
        Socket client = server.accept();
        DataInputStream in = new DataInputStream(client.getInputStream());

        List<String> segments = new ArrayList<>();
        while (true) {
            try {
                String s = in.readUTF();
                segments.add(s);
                if (showBinary) System.out.println("📥 Received: " + s);
            } catch (EOFException e) {
                break;
            }
        }

        String checksum = segments.remove(segments.size() - 1);
        boolean ok = verifyChecksum(segments, checksum);

        StringBuilder sb = new StringBuilder();
        for (String seg : segments)
            sb.append((char) Integer.parseInt(seg, 2));
        System.out.println("\n📝 Message: " + sb);
        System.out.println("🔎 Checksum status: " + (ok ? "VALID ✅" : "CORRUPTED ❌"));

        in.close();
        client.close();
        server.close();
    }

    static boolean verifyChecksum(List<String> segments, String checksum) {
        segments.add(checksum);
        int sum = 0;
        for (String seg : segments) {
            sum += Integer.parseInt(seg, 2);
            if (sum > 0xFF)
                sum = (sum & 0xFF) + 1;
        }
        return (sum & 0xFF) == 0xFF;
    }
}
