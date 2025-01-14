import java.net.InetAddress;

class Nithish_23BCE1230 {
    public Nithish_23BCE1230() {
        try {
            InetAddress ip = InetAddress.getLocalHost();            
            System.out.println("Hostname: " + ip.getHostName());
            System.out.println("IP Address: " + ip.getHostAddress());
        } catch (Exception e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
        }
    }
}

public class q1{
    public static void main(String[] args) {
        Nithish_23BCE1230 n = new Nithish_23BCE1230();
    }
}