import java.io.*;
import java.net.*;

public class c3 {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java CalculatorClient <num1> <num2> <operator>");
            return;
        }

        String num1 = args[0];
        String num2 = args[1];
        String operator = args[2];

        try (Socket socket = new Socket("127.0.0.1", 5001);
             DataInputStream dataIn = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Connected to the server!");

            // Send calculation details to the server
            dataOut.writeUTF(num1);
            dataOut.writeUTF(num2);
            dataOut.writeUTF(operator);

            // Receive and print the server's response
            String result = dataIn.readUTF();
            System.out.println("Result: " + result);

        } catch (IOException e) {
            System.err.println("Error: Unable to communicate with the server.");
            e.printStackTrace();
        }
    }
}
