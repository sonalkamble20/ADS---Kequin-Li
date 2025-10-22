//SONAL KAMBLE

import java.io.*;
import java.util.*;

public class ExpressionManipulation {

    static class Node {
        String token;
        Node left, right;
        Node(String t) { token = t; }
    }

    static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    static final Set<String> RIGHT_ASSOC = new HashSet<>();

    static {
        PRECEDENCE.put("||", 0);
        PRECEDENCE.put("&&", 1);
        PRECEDENCE.put("==", 2);
        PRECEDENCE.put("!=", 2);
        PRECEDENCE.put("<", 3);
        PRECEDENCE.put("<=", 3);
        PRECEDENCE.put(">", 3);
        PRECEDENCE.put(">=", 3);
        PRECEDENCE.put("+", 4);
        PRECEDENCE.put("-", 4);
        PRECEDENCE.put("*", 5);
        PRECEDENCE.put("/", 5);
        PRECEDENCE.put("%", 5);
        PRECEDENCE.put("^", 6);
        PRECEDENCE.put("!", 7);

        RIGHT_ASSOC.add("^");
        RIGHT_ASSOC.add("!");
    }

    static boolean isOperator(String t) { return PRECEDENCE.containsKey(t); }
    static boolean isOperand(String t) { return t.matches("-?\\d+"); }

    static List<String> infixToPostfix(List<String> tokens) {
        List<String> out = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String t : tokens) {
            if (t.equals("$")) {
                while (!stack.isEmpty()) out.add(stack.pop());
                out.add("$");
                break;
            } else if (isOperand(t)) out.add(t);
            else if (t.equals("(")) stack.push(t);
            else if (t.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) out.add(stack.pop());
                if (!stack.isEmpty()) stack.pop();
            } else if (isOperator(t)) {
                while (!stack.isEmpty() && !stack.peek().equals("(") && isOperator(stack.peek())) {
                    String top = stack.peek();
                    int a = PRECEDENCE.get(top), b = PRECEDENCE.get(t);
                    if (RIGHT_ASSOC.contains(t) ? a > b : a >= b) out.add(stack.pop());
                    else break;
                }
                stack.push(t);
            } else out.add(t);
        }
        return out;
    }

    static Node buildTree(List<String> postfix) {
        Deque<Node> st = new ArrayDeque<>();
        for (String t : postfix) {
            if (t.equals("$")) break;
            if (isOperand(t)) st.push(new Node(t));
            else if (isOperator(t)) {
                if (t.equals("!")) {
                    Node c = st.pop(); Node n = new Node(t); n.left = c; st.push(n);
                } else {
                    Node r = st.pop(), l = st.pop(); Node n = new Node(t);
                    n.left = l; n.right = r; st.push(n);
                }
            }
        }
        return st.pop();
    }

    static void printTree(Node n, int lvl) {
        if (n == null) return;
        printTree(n.right, lvl + 1);
        System.out.println("    ".repeat(lvl) + n.token);
        printTree(n.left, lvl + 1);
    }

    static String fullInfix(Node n) {
        if (n == null) return "";
        if (n.left == null && n.right == null) return n.token;
        if (n.token.equals("!")) return "(! " + fullInfix(n.left) + ")";
        return "(" + fullInfix(n.left) + " " + n.token + " " + fullInfix(n.right) + ")";
    }

    static int eval(Node n) {
        if (n.left == null && n.right == null) return Integer.parseInt(n.token);
        String op = n.token;
        if (op.equals("!")) return eval(n.left) == 0 ? 1 : 0;
        int L = eval(n.left), R = eval(n.right);
        return switch (op) {
            case "+" -> L + R;
            case "-" -> L - R;
            case "*" -> L * R;
            case "/" -> R == 0 ? 0 : L / R;
            case "%" -> R == 0 ? 0 : L % R;
            case "^" -> (int) Math.pow(L, R);
            case "<" -> L < R ? 1 : 0;
            case "<=" -> L <= R ? 1 : 0;
            case ">" -> L > R ? 1 : 0;
            case ">=" -> L >= R ? 1 : 0;
            case "==" -> L == R ? 1 : 0;
            case "!=" -> L != R ? 1 : 0;
            case "&&" -> (L != 0 && R != 0) ? 1 : 0;
            case "||" -> (L != 0 || R != 0) ? 1 : 0;
            default -> 0;
        };
    }

    static List<String> tokenize(String line) {
        List<String> t = new ArrayList<>(Arrays.asList(line.trim().split("\\s+")));
        if (!t.get(t.size() - 1).equals("$")) t.add("$");
        return t;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("/Users/sonalkamble/Desktop/Sonal/Desktop/ADS - Kequin Li/Assignment3/in.txt"));
        Scanner sc = new Scanner(System.in);
        String line; int i = 1;

        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            List<String> infix = tokenize(line);
            System.out.println("Expression " + i + ": " + String.join(" ", infix));

            List<String> postfix = infixToPostfix(infix);
            System.out.println("Postfix: " + String.join(" ", postfix));

            Node root = buildTree(postfix);
            System.out.println("Expression Tree:");
            printTree(root, 0);

            System.out.println("Fully Parenthesized: " + fullInfix(root));
            System.out.println("Value: " + eval(root));

            System.out.print("Press <Enter> to continue ...");
            sc.nextLine();
            i++;
            System.out.println();
        }
        sc.close();
    }
}
