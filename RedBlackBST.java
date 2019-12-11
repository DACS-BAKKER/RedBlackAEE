public class RedBlackBST<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        private int key;
        private T element;
        private boolean color;
        private Node left, right;

        public Node(int key, T element, boolean color) {
            this.key = key;
            this.element = element;
            this.color = color;
        }
    }

    private Node root;

    public RedBlackBST() {
        this.root = null;
    }

    // Returns true if tree contains key
    public boolean contains(int key) {
        return get(key) != null;
    }

    public void put(int key, T element) {
        root = put(root, key, element);
        root.color = BLACK;
    }

    private Node put(Node node, int key, T element) {
        if (node == null) return new Node(key, element, RED);

        if (key < node.key)
            node.left = put(node.left, key, element);
        else if (key > node.key)
            node.right = put(node.right, key, element);
        else
            node.element = element;

        if (isRed(node.right) && !isRed(node.left))
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            flipColors(node);

        return node;
    }

    private boolean isRed(Node node) {
        if (node == null)
            return false;
        return node.color;
    }

    private Node rotateLeft(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = temp.left.color;
        temp.left.color = RED;
        return temp;
    }

    private Node rotateRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = temp.right.color;
        temp.right.color = RED;
        return temp;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    // Returns the largest key in the symbol table.
    public int max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null)
            return x;
        else
            return max(x.right);
    }

    // Returns the smallest key in the symbol table.
    public int min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null)
            return x;
        else
            return min(x.left);
    }

    // Traverse and prints the keys in levelOrder order
    public void levelOrder() {
        int height = height();
        for (int i = 1; i <= height; i++)
            printLevel(root, i);
    }

    private void printLevel(Node root, int level) {
        if (root == null)
            return;
        if (level == 1)
            System.out.print(root.element + " ");
        else if (level > 1) {
            printLevel(root.left, level - 1);
            printLevel(root.right, level - 1);
        }
    }

    // Returns height
    public int height() {
        return height(root) + 1;
    }

    private int height(Node node) {
        if (node == null)
            return -1;
        return Math.max(height(node.left), height(node.right)) + 1;
    }


    // Traverse and prints the keys in inOrder order
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }
        // Recursion on left subtree
        inOrder(node.left);
        System.out.print(node.element + " ");
        // Recursion on right subtree
        inOrder(node.right);
    }

    // Traverse and prints the keys in postOrder order
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node node) {
        if (node == null)
            return;
        // Recursion on left subtree
        postOrder(node.left);
        // Recursion on right subtree
        postOrder(node.right);
        // Print data of root node
        System.out.print(node.element + " ");
    }

    // Traverse and prints the keys in preOrder order
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {
        if (node == null)
            return;
        // Print data of root node
        System.out.print(node.element + " ");
        // Recursion on left subtree
        preOrder(node.left);
        // Recursion on right subtree
        preOrder(node.right);
    }

    // Returns T based on key
    public T get(int key) {
        return get(root, key);
    }

    private T get(Node node, int key) {
        if (node == null)
            return null;
        // Smaller on left side
        if (key < node.key)
            return get(node.left, key);
            // Bigger on right side
        else if (key > node.key)
            return get(node.right, key);
        else
            return node.element;
    }

    private Node deleteMin(Node node) {
        if (node.left == null)
            return null;

        if (!isRed(node.left) && !isRed(node.left.left)) {
            flipColors(node);
            if (isRed(node.right.left)) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
                flipColors(node);
            }
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node balance(Node node) {
        if (isRed(node.right))
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            flipColors(node);
        return node;
    }

    public void delete(int key) {
        if (!contains(key)) return;
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (root != null)
            root.color = BLACK;
    }

    private Node delete(Node node, int key) {
        if (key < node.key)  {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                flipColors(node);
                if (isRed(node.right.left)) {
                    node.right = rotateRight(node.right);
                    node = rotateLeft(node);
                    flipColors(node);
                }
            }
            node.left = delete(node.left, key);
        }
        else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key == node.key && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left)) {
                flipColors(node);
                if (isRed(node.left.left)) {
                    node = rotateRight(node);
                    flipColors(node);
                }
            }
            if (node.key == key) {
                Node x = min(node.right);
                node.key = x.key;
                node.element = x.element;
                node.right = deleteMin(node.right);
            }
            else node.right = delete(node.right, key);
        }
        return balance(node);
    }


    public static void main(String[]args) {
        RedBlackBST<Integer> tree = new RedBlackBST<Integer>();
        for (int i = 1; i < 10; i++)
            tree.put(i,i);

        tree.inOrder();
        System.out.println("");

        tree.delete(2);
        tree.inOrder();
    }
}
