import java.io.*;
import java.net.*;
import java.util.Scanner;

class Server{
	public static void main(String args[]) throws IOException{
		ServerSocket server = new ServerSocket(42069);
		Socket client =  server.accept();

		DataInputStream in = new DataInputStream(client.getInputStream());
		DataOutputStream out = new DataOutputStream(client.getOutputStream());

		String msg = in.readUTF();
		System.out.println("client>> " + msg);

		Scanner scan = new Scanner(System.in);
		String serve = scan.nextLine();

		out.writeUTF(serve);

		in.close();
		scan.close();
		out.close();
	}
}
