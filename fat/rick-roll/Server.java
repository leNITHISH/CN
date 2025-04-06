import java.io.*;
import java.net.*;

class Server{
	public static void main(String args[])throws IOException {
		ServerSocket server = new ServerSocket(42069);
		while(true) new Thread(new Handler(server.accept())).start();
	}
}

class Handler implements Runnable{
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;

	public Handler(Socket c)throws IOException{
		this.client = c;
		this.in = new DataInputStream(client.getInputStream());
		this.out = new DataOutputStream(client.getOutputStream());
	}
    public void run() {
        try {
            while (true) {
                String input = in.readUTF().trim();
                if (input.equals("69")) out.writeUTF(lyrics);
                else out.writeUTF("Try 69 😉");
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }

    private static final String lyrics = """
        [Pre-Chorus]
        And if you ask me how I'm feeling
        Don't tell me you're too blind to see

        [Chorus]
        Never gonna give you up
        Never gonna let you down
        Never gonna run around and desert you
        Never gonna make you cry
        Never gonna say goodbye
        Never gonna tell a lie and hurt you
        Never gonna give you up
        Never gonna let you down
        Never gonna run around and desert you
        Never gonna make you cry
        Never gonna say goodbye
        Never gonna tell a lie and hurt you

        [Bridge]
        Ooh (Give you up)
        Ooh-ooh (Give you up)
        Ooh-ooh
        Never gonna give, never gonna give (Give you up)
        Ooh-ooh
        Never gonna give, never gonna give (Give you up)

        [Verse 2]
        We've known each other for so long
        Your heart's been aching, but you're too shy to say it (To say it)
        Inside, we both know what's been going on (Going on)
        We know the game, and we're gonna play it

        [Pre-Chorus]
        I just wanna tell you how I'm feeling
        Gotta make you understand

        [Chorus]
        Never gonna give you up
        Never gonna let you down
        Never gonna run around and desert you
        Never gonna make you cry
        Never gonna say goodbye
        Never gonna tell a lie and hurt you
        Never gonna give you up
        Never gonna let you down
        Never gonna run around and desert you
        Never gonna make you cry
        Never gonna say goodbye
        Never gonna tell a lie and hurt you
        Never gonna give you up
        Never gonna let you down
        Never gonna run around and desert you
        Never gonna make you cry
        Never gonna say goodbye
        Never gonna tell a lie and hurt you
        """;
}


