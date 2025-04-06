
import java.net.*;
import java.util.Random;

class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(9999);
        String msg = "Never gonna give you up";
        String generator = "1001";

        String data = stringToBinary(msg);
        String encoded = crcEncode(data, generator);
        String corrupted = injectError(encoded);

        byte[] buf = corrupted.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 8888);
        socket.send(packet);

        System.out.println("📤 Sent corrupted message: " + binaryToString(corrupted));
        System.out.println("🧬 Corrupted binary: " + corrupted);
        socket.close();
    }

    static String stringToBinary(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray())
            sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        return sb.toString();
    }

    static String binaryToString(String bin) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bin.length(); i += 8) {
            String byteStr = bin.substring(i, Math.min(i + 8, bin.length()));
            sb.append((char) Integer.parseInt(byteStr, 2));
        }
        return sb.toString();
    }

    static String crcEncode(String data, String generator) {
        StringBuilder dividend = new StringBuilder(data);
        dividend.append("0".repeat(generator.length() - 1));

        for (int i = 0; i <= dividend.length() - generator.length(); ) {
            for (int j = 0; j < generator.length(); j++)
                dividend.setCharAt(i + j, dividend.charAt(i + j) == generator.charAt(j) ? '0' : '1');
            while (i < dividend.length() && dividend.charAt(i) != '1') i++;
        }

        return data + dividend.substring(data.length());
    }

    static String injectError(String input) {
        Random rand = new Random();
        int bit = rand.nextInt(input.length());
        char flipped = input.charAt(bit) == '0' ? '1' : '0';
        return input.substring(0, bit) + flipped + input.substring(bit + 1);
    }
}
