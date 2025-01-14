import java.io.*;
import java.net.*;

class Nithish_23BCE1230 {
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;

    public Nithish_23BCE1230(String addr, int port) {
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

}

public class c1{
    public static void main(String[] args) {
        Nithish_23BCE1230 client = new Nithish_23BCE1230("127.0.0.1", 42069);
    }
}