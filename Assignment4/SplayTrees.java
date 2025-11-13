import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SplayTrees {

    // ===================== NODE CLASS =====================
    private static class Node {
        int key;
        Node left, right, parent;

        Node(int key) {
            this.key = key;
        }
    }

    // ===================== SPLAY TREE CLASS =====================
    private static class SplayTree {
        private Node root;

        // ---------- Initial BST insert (NO splaying) ----------
        public void bstInsert(int key) {
            if (root == null) {
                root = new Node(key);
                return;
            }
            Node current = root;
            Node parent = null;
            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    // Ignore duplicates when building initial tree
                    return;
                }
            }
            Node newNode = new Node(key);
            newNode.parent = parent;
            if (key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }

        // ---------- Standard left rotation ----------
        private void rotateLeft(Node x) {
            if (x == null) return;
            Node y = x.right;
            if (y == null) return;

            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }

            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }

            y.left = x;
            x.parent = y;
        }

        // ---------- Standard right rotation ----------
        private void rotateRight(Node x) {
            if (x == null) return;
            Node y = x.left;
            if (y == null) return;

            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }

            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }

            y.right = x;
            x.parent = y;
        }

        // ---------- Splay operation ----------
        private void splay(Node x) {
            if (x == null) return;

            while (x.parent != null) {
                Node p = x.parent;
                Node g = p.parent;

                if (g == null) {
                    // Zig step
                    if (x == p.left) {
                        rotateRight(p);
                    } else {
                        rotateLeft(p);
                    }
                } else if (x == p.left && p == g.left) {
                    // Zig-zig (left-left)
                    rotateRight(g);
                    rotateRight(p);
                } else if (x == p.right && p == g.right) {
                    // Zig-zig (right-right)
                    rotateLeft(g);
                    rotateLeft(p);
                } else if (x == p.right && p == g.left) {
                    // Zig-zag (left-right)
                    rotateLeft(p);
                    rotateRight(g);
                } else if (x == p.left && p == g.right) {
                    // Zig-zag (right-left)
                    rotateRight(p);
                    rotateLeft(g);
                }
            }
        }

        // ---------- Splay at given key (for S command) ----------
        // Returns true if key is found and splayed, false if not found
        public boolean splayAt(int key) {
            Node current = root;
            Node last = null;
            while (current != null) {
                last = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    // Found exact key
                    splay(current);
                    return true;
                }
            }
            // Key not found: optional splay at last accessed node
            if (last != null) {
                splay(last);
            }
            return false;
        }

        // ---------- Find (search + splay) ----------
        // Returns true if found; false otherwise
        public boolean find(int key) {
            Node current = root;
            Node last = null;
            while (current != null) {
                last = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    // Found key
                    splay(current);
                    return true;
                }
            }
            // Not found; splay last accessed node (typical splay behavior)
            if (last != null) {
                splay(last);
            }
            return false;
        }

        // ---------- Insert (with splaying) ----------
        // Returns true if inserted, false if duplicate
        public boolean insert(int key) {
            if (root == null) {
                root = new Node(key);
                return true;
            }
            Node current = root;
            Node parent = null;

            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    // Duplicate key: splay the existing node and fail insert
                    splay(current);
                    return false;
                }
            }

            Node newNode = new Node(key);
            newNode.parent = parent;

            if (key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            // Splay newly inserted node to the root
            splay(newNode);
            return true;
        }

        // ---------- Delete (with splaying) ----------
        // Returns true if successfully deleted, false if key not present
        public boolean delete(int key) {
            if (root == null) return false;

            // First, splay the key to the root if it exists
            if (!find(key)) {
                // find() already splayed last accessed node
                return false;
            }

            // Now root.key == key (because find succeeded)
            Node leftSub = root.left;
            Node rightSub = root.right;

            if (leftSub != null) {
                leftSub.parent = null;
            }
            if (rightSub != null) {
                rightSub.parent = null;
            }

            if (leftSub == null) {
                // Just replace root with right subtree
                root = rightSub;
            } else {
                // Find max in leftSub and splay it to root, then attach rightSub
                Node max = leftSub;
                while (max.right != null) {
                    max = max.right;
                }
                splay(max); // Splay max of leftSub to root
                // Now root has no right child
                root.right = rightSub;
                if (rightSub != null) {
                    rightSub.parent = root;
                }
            }
            return true;
        }

        // ---------- Public print method ----------
        public void printTree() {
            if (root == null) {
                System.out.println("(empty tree)");
            } else {
                printTree(root, 0);
            }
        }

        // Sideways tree print: right subtree on top, root in middle, left below
        private void printTree(Node node, int depth) {
            if (node == null) return;

            printTree(node.right, depth + 1);

            for (int i = 0; i < depth; i++) {
                System.out.print("    ");
            }
            System.out.println(node.key);

            printTree(node.left, depth + 1);
        }
    }

    // ===================== MAIN PROGRAM =====================
    public static void main(String[] args) {
        SplayTree tree = new SplayTree();

        // --------- Phase 1: Build initial BST from in.dat ---------
        try {
            Scanner fileScanner = new Scanner(new File("/Users/sonalkamble/Desktop/Sonal/Desktop/ADS - Kequin Li/Assignment4/in.dat"));
            while (fileScanner.hasNextInt()) {
                int key = fileScanner.nextInt();
                tree.bstInsert(key);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: in.dat not found. Starting with an empty tree.");
        }

        System.out.println("Initial tree:");
        tree.printTree();
        System.out.println();

        // --------- Phase 2: Interactive commands ---------
        Scanner input = new Scanner(System.in);
        System.out.println("Commands:");
        System.out.println("  S x  - splay the tree at key x");
        System.out.println("  F x  - search/find key x");
        System.out.println("  I x  - insert key x");
        System.out.println("  D x  - delete key x");
        System.out.println("  Q    - quit");
        System.out.println();

        while (true) {
            System.out.print("> ");
            if (!input.hasNext()) {
                break; // EOF
            }

            String cmd = input.next();
            if (cmd.equalsIgnoreCase("Q")) {
                break;
            }

            // Commands S/F/I/D must be followed by an integer
            if (!cmd.equalsIgnoreCase("S") &&
                    !cmd.equalsIgnoreCase("F") &&
                    !cmd.equalsIgnoreCase("I") &&
                    !cmd.equalsIgnoreCase("D")) {
                System.out.println("Invalid command. Use S, F, I, D, or Q.");
                input.nextLine(); // discard rest of line
                continue;
            }

            if (!input.hasNextInt()) {
                System.out.println("A key (integer) is required after the command.");
                input.nextLine(); // discard rest of line
                continue;
            }

            int key = input.nextInt();

            char c = Character.toUpperCase(cmd.charAt(0));
            boolean result;

            switch (c) {
                case 'S':
                    result = tree.splayAt(key);
                    if (result) {
                        System.out.println("Splay is done");
                    } else {
                        System.out.println("The key is not in the tree");
                    }
                    break;

                case 'F':
                    result = tree.find(key);
                    if (result) {
                        System.out.println("Search is successful");
                    } else {
                        System.out.println("Search is unsuccessful");
                    }
                    break;

                case 'I':
                    result = tree.insert(key);
                    if (result) {
                        System.out.println("The key is inserted into the tree");
                    } else {
                        System.out.println("Duplicated keys");
                    }
                    break;

                case 'D':
                    result = tree.delete(key);
                    if (result) {
                        System.out.println("The key is deleted from the tree");
                    } else {
                        System.out.println("The key is not in the tree");
                    }
                    break;

                default:
                    System.out.println("Unknown command.");
            }

            // Display the tree after each command
            tree.printTree();
            System.out.println();
        }

        input.close();
        System.out.println("Program terminated.");
    }
}
