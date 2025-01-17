/*
    upp
    low
    alt
    rev
*/ 


import java.io.*;
import java.net.*;

public class s2 {
private static DataInputStream din;
private static DataOutputStream dout;
private static String b(String str){
    int number = Integer.parseInt(str);
    if (number == 0) {
        return "0";
    }

    StringBuilder binary = new StringBuilder();
    while (number > 0) {
        binary.append(number % 2);
        number /= 2;
    }

    // Reverse the string to get the correct binary representation
    return binary.reverse().toString();
}
private static String o(String str){
    int number = Integer.parseInt(str);
    if (number == 0) {
        return "0";
    }

    StringBuilder octal = new StringBuilder();
    
    while (number > 0) {
        octal.append(number % 8);
        number /= 8;
    }

    return octal.reverse().toString();
}
private static String x(String str){
    int number = Integer.parseInt(str);

    if (number == 0) {
        return "0";
    }

    StringBuilder hex = new StringBuilder();
    char[] hexDigits = "0123456789ABCDEF".toCharArray();

    while (number > 0) {
        hex.append(hexDigits[number % 16]);
        number /= 16;
    }

    return hex.reverse().toString();
}

public static void main(String[] args) throws IOException {


ServerSocket serverSocket = new ServerSocket(5001);
System.out.println("Server Listening on port 5001...");



while (true) {
    try{
Socket connectionSocket = serverSocket.accept();
din = new DataInputStream(connectionSocket.getInputStream());
dout = new DataOutputStream(connectionSocket.getOutputStream());

new Thread (() -> { try{
    String str = din.readUTF();
    String op = din.readUTF();
    if(op.equals("b"))dout.writeUTF(b(str));
    else if(op.equals("o"))dout.writeUTF(o(str));
    else if(op.equals("x"))dout.writeUTF(x(str));
    else dout.writeUTF("Error:");
}catch(IOException q){
    q.printStackTrace();
}
}).start();
    } catch (IOException i){
        System.err.println("Closing Connection");
        break;
    }
}

    try{
        din.close();
        dout.close();
        serverSocket.close();
    } catch (IOException i) {
            System.out.println(i);
        }


}
}
