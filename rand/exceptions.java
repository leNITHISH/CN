import java.io.*;

public class exceptions {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("file.txt");
        } catch (FileNotFoundException e) {
            System.out.println("\nfile not found\n" + e.getMessage()+"\n\n");
        }

        try {
            int[] nums = {1, 2, 3};
            System.out.println(nums[5]); 
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("index outta bounds\n" + e.getMessage());
        }
    }
}
