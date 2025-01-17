/*
    upp
    low
    alt
    rev
*/ 


import java.io.*;
import java.net.*;

public class s1 {
private static DataInputStream din;
private static DataOutputStream dout;
private static String upp(String str){
    return str.toUpperCase();
}
private static String low(String str){
    return str.toLowerCase();
}
private static String alt(String str){
    StringBuilder s = new StringBuilder(); 
    for(int i=0;i<str.length();i++){
        char c = str.charAt(i);
        if(Character.isUpperCase(c))s.append(Character.toLowerCase(c));
        else if(Character.isLowerCase(c))s.append(Character.toUpperCase(c));
        else s.append(c);
    }
    return s.toString();
}
private static String rev(String str){
    StringBuilder s = new StringBuilder();
    s.append(str);
    s.reverse();
    return s.toString();
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
    if(op.equals("upp"))dout.writeUTF(upp(str));
    else if(op.equals("low"))dout.writeUTF(low(str));
    else if(op.equals("alt"))dout.writeUTF(alt(str));
    else if(op.equals("rev"))dout.writeUTF(rev(str));
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
