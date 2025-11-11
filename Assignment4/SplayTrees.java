import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SplayTrees
{
    Node root;

    void insert(int value)
    {
        root = recursiveInsert(root, value);
    }

    Node recursiveInsert(Node root, int value)
    {
        if(root == null)
            root = new Node(value);

        else if(value < root.data)
            root.left = recursiveInsert(root.left, value);
        else
            root.right = recursiveInsert(root.right, value);

        return root;
    }


    public static void main(String[] args)
    {
        SplayTrees st = new SplayTrees();

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

            st.insert(Integer.parseInt(str));

            System.out.println(str);
        }
        System.out.println("Bye-bye!");

    }


}


class Node
{
    Integer data;
    Node left;
    Node right;

    public Node(Integer data) {
        this.data = data;
        left = right = null;
    }
}
