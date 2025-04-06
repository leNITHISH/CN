
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        boolean showBinary = false;
        int corruptIndex = -1;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-b")) showBinary = true;
            if (args[i].equals("-e") && i + 1 < args.length)
                corruptIndex = Integer.parseInt(args[++i]);
        }

        Socket socket = new Socket("localhost", 6969);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String msg = "Never gonna give you up";
        List<String> segments = new ArrayList<>();

        for (char c : msg.toCharArray())
            segments.add(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));

        String checksum = calculateChecksum(segments);
        segments.add(checksum);

        if (corruptIndex >= 0 && corruptIndex < segments.size()) {
            // Flip a random bit in the segment
            String corrupt = segments.get(corruptIndex);
            char[] bits = corrupt.toCharArray();
            int bitToFlip = new Random().nextInt(8);
            bits[bitToFlip] = bits[bitToFlip] == '0' ? '1' : '0';
            segments.set(corruptIndex, new String(bits));
            System.out.println("💥 Injected error in segment " + corruptIndex);
        }

        for (String segment : segments) {
            out.writeUTF(segment);
            if (showBinary) System.out.println("📤 Sent: " + segment);
        }

        out.close();
        socket.close();
    }

    static String calculateChecksum(List<String> segments) {
        int sum = 0;
        for (String seg : segments) {
            sum += Integer.parseInt(seg, 2);
            if (sum > 0xFF)
                sum = (sum & 0xFF) + 1;
        }
        return String.format("%8s", Integer.toBinaryString(~sum & 0xFF)).replace(' ', '0');
    }
}
