import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SplayTrees
{
    public static void main(String[] args) {

        Scanner sc;
        
        try{
            sc = new Scanner(new File("/Users/sonalkamble/Desktop/Sonal/Desktop/ADS - Kequin Li/Assignment4/in.dat"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hello! This is a Splay Trees Program.");
        System.out.println();

        while(sc.hasNextLine())
        {
            String str = sc.nextLine();
            System.out.println(str);
        }
        System.out.println("Bye-bye!");
    }
}
