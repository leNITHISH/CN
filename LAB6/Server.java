import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {
    // Shared list to store client uploads
    public static final List<ClientData> clients = new ArrayList<>();
    public static final Object lock = new Object();
    public static boolean computed = false;

    public static void main(String[] args) {
        // Create uploads folder if it doesn't exist
        File uploadsDir = new File("uploads");
        if (!uploadsDir.exists()) {
            uploadsDir.mkdir();
        }
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Compute the Jaccard similarity between two texts (as a percentage)
    public static double jaccardSimilarity(String text1, String text2) {
        String[] words1 = text1.split("\\W+");
        String[] words2 = text2.split("\\W+");
        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        if (union.isEmpty()) {
            return 0.0;
        }
        return (intersection.size() / (double) union.size()) * 100;
    }
}

class ClientData {
    Socket socket;
    String fileName;
    String fileContent;
    String similarityMessage;
}

class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
             
            // Read file name and file content from client
            String fileName = dis.readUTF();
            int fileLength = dis.readInt();
            byte[] fileBytes = new byte[fileLength];
            dis.readFully(fileBytes);
            String fileContent = new String(fileBytes, StandardCharsets.UTF_8);

            // Save file to "uploads" folder
            File outFile = new File("uploads" + File.separator + fileName);
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(fileBytes);
            }

            // Create a ClientData object and add it to the shared list
            ClientData cd = new ClientData();
            cd.socket = socket;
            cd.fileName = fileName;
            cd.fileContent = fileContent;
            
            synchronized (Server.lock) {
                Server.clients.add(cd);
                // If not all three clients have connected, wait
                if (Server.clients.size() < 3) {
                    while (Server.clients.size() < 3 || !Server.computed) {
                        Server.lock.wait();
                    }
                } else {
                    // This is the third client. If similarities are not yet computed, do it now.
                    if (!Server.computed) {
                        for (ClientData c : Server.clients) {
                            StringBuilder sb = new StringBuilder();
                            for (ClientData other : Server.clients) {
                                if (!c.fileName.equals(other.fileName)) {
                                    double sim = Server.jaccardSimilarity(c.fileContent, other.fileContent);
                                    sb.append("Similarity with ").append(other.fileName)
                                      .append(": ").append(String.format("%.2f", sim)).append("%\n");
                                }
                            }
                            c.similarityMessage = sb.toString();
                        }
                        Server.computed = true;
                    }
                    Server.lock.notifyAll();
                }
            }
            // Send the similarity result to this client
            dos.writeUTF(cd.similarityMessage);
            dos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ex) { }
        }
    }
}
