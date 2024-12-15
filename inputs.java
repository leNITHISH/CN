import java.io.*;
import java.util.Scanner;

public class inputs {
    public static void main(String args[]){
        inputs in = new inputs();
        //iSystem.out.println("\nbuffer:\n\t"+in.buf());
        System.out.println("\nscanner:\n\t"+in.sca());
        // System.out.println("\nconsole:\n\t"+in.con());

    }

    public String buf() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            return br.readLine();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String sca() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public String con() {
        Console console = System.console();
        if (console != null)return console.readLine();
        else return "err";
    }
}
