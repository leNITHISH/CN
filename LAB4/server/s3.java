import java.io.*;
import java.net.*;

public class s3 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5001);
        System.out.println("Server Listening on port 5001...");

        while (true) {
            Socket connectionSocket = serverSocket.accept();
            new Thread(new ClientHandler(connectionSocket)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket connectionSocket;

    public ClientHandler(Socket socket) {
        this.connectionSocket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataIn = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(connectionSocket.getOutputStream());

            // Read client input: two numbers and an operator
            Float num1 = Float.parseFloat(dataIn.readUTF());
            Float num2 = Float.parseFloat(dataIn.readUTF());
            String operator = dataIn.readUTF();

            // Perform calculation based on the operator
            String result;
            switch (operator) {
                case "A": // A is for addition
                    result = String.valueOf(num1 + num2);
                    break;
                case "S": // S is for subtraction
                    result = String.valueOf(num1 - num2);
                    break;
                case "M": // M is for multiplication
                    result = String.valueOf(num1 * num2);
                    break;
                case "D": // D is for division
                    if (num2 != 0) {
                        result = String.valueOf(num1 / num2);
                    } else {
                        result = "Error: Division by zero";
                    }
                    break;
                default:
                    result = "Error: Invalid operator";
                    break;
            }

            // Send result to client
            dataOut.writeUTF(result);
            connectionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
