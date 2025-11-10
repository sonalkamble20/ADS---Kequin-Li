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

            Node root = new Node(Integer.parseInt(str), null, null);

            System.out.println(str);
        }
        System.out.println("Bye-bye!");
    }
}


class Node
{
    private Integer data;
    private Node left;
    private Node right;

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node(Integer data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public void addAfter(Node root)
    {

    }
}
