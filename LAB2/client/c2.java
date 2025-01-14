import java.io.*;
import java.net.*;
import java.util.Scanner;

import java.io.*;
import java.net.*;

class Nithish_23BCE1230 {
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;

     Nithish_23BCE1230(String addr, int port) {
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
                if(data.equals("end"))break;
                out.writeUTF(data);
                out.flush();

                DataInputStream serverIn = new DataInputStream(socket.getInputStream());
                String serverMessage = serverIn.readUTF();
                System.out.println("Server: "+serverMessage);
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


public class c2{
    public static void main(String[] args) {
        Nithish_23BCE1230 client = new Nithish_23BCE1230("127.0.0.1", 42069);
    }
}