import java.util.Scanner;
import java.io.*;
import java.net.*;

class Client{
	public static void main(String args[]) throws IOException{
		Socket socket = new Socket("localhost",42069);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		Scanner scan = new Scanner(System.in);
		
		System.out.println("enter smth");
		String message = scan.nextLine();
		out.writeUTF(message);

		String inp = in.readUTF();
		System.out.println("server>> "+inp);

		in.close();
		out.close();
		scan.close();
	}
}
