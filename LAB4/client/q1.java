import java.io.*;
import java.net.*;

public class q1 {
private static Socket socket;
private static DataInputStream dataIn;
private static DataOutputStream dataOut;

public static void main(String [] args) throws IOException {
String str1 = args[0];
String operation = args[1];
// String operator = args[2];

socket = new Socket();
socket.connect(new InetSocketAddress("6cfd4e07-b5e5-48de-bb61-e840336de517.deepnoteproject.com", 8080), 10000);
System.out.println("Connection Successful!");

dataIn = new DataInputStream(socket.getInputStream());
dataOut = new DataOutputStream(socket.getOutputStream());

dataOut.writeUTF(str1);
dataOut.writeUTF(operation);

// dataOut.writeUTF(num2);

String serverMessage = dataIn.readUTF();
System.out.println("Result: " + serverMessage);
}
}
