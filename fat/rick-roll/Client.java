import java.util.Scanner;
import java.io.*;
import java.net.*;

class Client{
	public static void main(String args[]) throws IOException{
		Socket socket = new Socket("localhost", 42069);

		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		Scanner sc = new Scanner(System.in);

		System.out.println("enter an integer:");
		String n = sc.nextLine();
		
		out.writeUTF(n);
		String msg = in.readUTF();
		System.out.println(msg);

		in.close();
		out.close();
		sc.close();
	}
}
