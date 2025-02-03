import java.io.*;
import java.net.*;
import java.util.*;
public class q1 {
private static Socket socket;
private static DataInputStream dataIn;
private static DataOutputStream dataOut;
private static String str1 = "HELLO";
public static String toB(int n){
    String bin = "";
    while(n!=0){
        if(n%2==0)bin = "0"+bin;
        else bin = "1"+bin;
        n/=2;
    }
    return bin;
}
public static void main(String [] args) throws IOException {
socket = new Socket();
socket.connect(new InetSocketAddress("127.0.0.1", 5001), 1000);
System.out.println("Connection Successful!");

dataIn = new DataInputStream(socket.getInputStream());
dataOut = new DataOutputStream(socket.getOutputStream());

int sum = str1.charAt(0);

for(int i = 1;i<str1.length();i++)sum^=(int)(str1.charAt(i));
str1 = "HALLO";
StringBuilder s = new StringBuilder(str1);
s.append((char)sum);
dataOut.writeUTF(s.toString());
String send = s.toString();
for(int i=0;i<send.length()-1;i++)System.out.printf("%s : %c\n", toB(send.charAt(i)), send.charAt(i));
System.out.printf("Parity byte: %s\n", toB(send.charAt(send.length()-1)));
System.out.println("Message: "+ "HELLO\n" + "Sent: "+ s.toString());
}
}