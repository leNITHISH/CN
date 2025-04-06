
import java.net.*;

class Client {
    public static void main(String[] args) throws Exception {
        boolean showBinary = args.length > 0 && args[0].equals("-b");
        DatagramSocket socket = new DatagramSocket(8888);
        byte[] buf = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String received = new String(packet.getData(), 0, packet.getLength());

        System.out.println("📥 Received: " + (showBinary ? received : binaryToString(received)));

        String generator = "1001";
        boolean hasError = crcCheck(received, generator);

        System.out.println(hasError ? "❌ Error detected in transmission!" : "✅ No error detected.");
        socket.close();
    }

    static boolean crcCheck(String data, String generator) {
        StringBuilder remainder = new StringBuilder(data);

        for (int i = 0; i <= remainder.length() - generator.length(); ) {
            for (int j = 0; j < generator.length(); j++)
                remainder.setCharAt(i + j, remainder.charAt(i + j) == generator.charAt(j) ? '0' : '1');
            while (i < remainder.length() && remainder.charAt(i) != '1') i++;
        }

        return !remainder.substring(data.length() - generator.length() + 1).equals("0".repeat(generator.length() - 1));
    }

    static String binaryToString(String bin) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bin.length(); i += 8) {
            String byteStr = bin.substring(i, Math.min(i + 8, bin.length()));
            sb.append((char) Integer.parseInt(byteStr, 2));
        }
        return sb.toString();
    }
}
