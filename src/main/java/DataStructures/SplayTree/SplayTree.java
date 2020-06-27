package DataStructures.SplayTree;

class Node {
    private int data;
    Node parent;
    Node left;
    Node right;
    boolean reversalBit;


    public Node(int data) {
        this.data = data;
    }


    public int getData() {
        return data;
    }


    void cleanReversalBit() {
        if (reversalBit) {
            Node intermediate = left;
            left = right;
            right = intermediate;
            right.complementReversalBit();
            left.complementReversalBit();
        }
    }


    void complementReversalBit() {
        if (reversalBit) {
            reversalBit = false;
        } else {
            reversalBit = true;
        }
    }


    /**
     * @param start
     * @param array
     * @return what is the next position to fill
     */
    int inorder(int start, int[] array) {
        if (reversalBit) {
            int rightend = right.inorder(start, array);
            array[rightend] = data;
            return left.inorder(rightend + 1, array);

        } else {
            int leftEnd = left.inorder(start, array);
            array[leftEnd] = data;
            return right.inorder(leftEnd + 1, array);
        }
    }
}


/**
 * This class represents the splay tree.
 * <p>
 * TODO: do between and flip
 */

public class SplayTree {

    private Node root;
    private int numberNodes; //this is only given since I just want to use this implementation for TSP.
    private Node[] nodesArray;


    public SplayTree(int[] tour) {
        this.numberNodes = tour.length;
        nodesArray = new Node[numberNodes];
        build(tour);
    }


    /**
     * This method does a left Rotation around node x.
     *
     * @param x the node around which to rotate
     */
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }


    /**
     * This method does a right Rotation around node x.
     *
     * @param x the node around which to rotate
     */
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }


    private void zick(Node x) {
        if (x.parent == null || x.parent.left != x) {
            return;
        }
        rightRotate(x.parent);
    }


    private void zack(Node x) {
        if (x.parent == null || x.parent.right != x) {
            return;
        }
        leftRotate(x.parent);
    }


    private void zickZick(Node x) {
        Node parent = x.parent;
        if (parent == null) {
            return;
        }
        zick(parent);
        zick(x);
    }


    private void zackZack(Node x) {
        Node parent = x.parent;
        if (parent == null) {
            return;
        }
        zack(parent);
        zack(x);
    }


    private void zackZick(Node x) {
        zack(x);
        zick(x);
    }


    private void zickZack(Node x) {
        zick(x);
        zack(x);
    }


    private void splay(Node x) {
        while (true) {
            if (x.parent == null) {
                return;
            } else if (x.parent.parent == null) {
                x.parent.cleanReversalBit();
                x.cleanReversalBit();
                if (x.parent.left == x) {//x is a left child
                    zick(x);
                } else { //x is a right child
                    zack(x);
                }
            } else {
                x.parent.parent.cleanReversalBit();
                x.parent.cleanReversalBit();
                x.cleanReversalBit();
                if (x.parent.left == x) {//x is a left child
                    if (x.parent.parent.left == x.parent) { //parent of x is a left child
                        zickZick(x);
                    } else {//parent of x is a right child
                        zickZack(x);
                    }
                } else { //x is a right child
                    if (x.parent.parent.left == x) {//parent of x is a left child
                        zackZick(x);
                    } else {//parent of x is a right child
                        zackZack(x);
                    }
                }
            }
        }
    }


    public Node search(int data) {
        if (data >= nodesArray.length || data < 0) {
            return null;
        }
        splay(nodesArray[data]);
        return root;
    }


    private Node makeTree(int left, int right, int[] array) {
        if (left > right)
            return null;

        // Get the middle element and make it root
        int middle = (left + right) / 2;
        Node middleNode = new Node(array[middle]);
        nodesArray[array[middle]] = middleNode;

        //Construct left tree
        middleNode.left = makeTree(left, middle - 1, array);
        if (middleNode.left != null) {
            middleNode.left.parent = middleNode;
        }

        //Construct right subtree
        middleNode.right = makeTree(middle + 1, right, array);
        if (middleNode.right != null) {
            middleNode.right.parent = middleNode;
        }

        return middleNode;
    }


    private void build(int[] tour) {
        root = makeTree(0, tour.length - 1, tour);
    }


    /**
     * finds the first node in the tour rooted by node.
     *
     * @param node
     * @return
     */
    public int findMin(Node node, boolean reverse) {
        if (node.reversalBit ^ reverse) {
            if (node.right == null) {
                return node.getData();
            } else {
                return findMin(node.right, true);
            }
        } else {
            if (node.left == null) {
                return node.getData();
            } else {
                return findMin(node.left, false);
            }
        }
    }


    /**
     * finds the first node in the tour rooted by node.
     *
     * @param node
     * @return
     */
    public int findMax(Node node, boolean reverse) {
        if (node.reversalBit ^ reverse) {
            if (node.left == null) {
                return node.getData();
            } else {
                return findMin(node.right, true);
            }
        } else {
            if (node.left == null) {
                return node.getData();
            } else {
                return findMin(node.left, false);
            }
        }
    }


    public int next(int city) {
        if (city >= nodesArray.length || city < 0 || nodesArray[city] == null) {
            return -1;
        }
        splay(nodesArray[city]);
        Node node = nodesArray[city];
        if (node.right == null && !node.reversalBit) {
            return findMin(node, false);
        } else if (node.right == null && node.reversalBit) {
            if (node.left == null) {
                return city;
            }
            return findMin(node.left, true);
        } else if (node.right != null && !node.reversalBit) {
            return findMax(node.right, false);
        } else {
            return findMax(node.right, true);
        }
    }


    public int prev(int city) {
        if (city >= nodesArray.length || city < 0 || nodesArray[city] == null) {
            return -1;
        }
        splay(nodesArray[city]);
        Node node = nodesArray[city];
        if (node.left == null && !node.reversalBit) {
            return findMax(node, false);
        } else if (node.left == null && node.reversalBit) {
            if (node.right == null) {
                return city;
            }
            return findMax(node.right, true);
        } else if (node.left != null && !node.reversalBit) {
            return findMin(node.left, false);
        } else {
            return findMin(node.left, true);
        }
    }


    public int[] inorder() {
        int[] result = new int[numberNodes];
        root.inorder(0, result);
        return result;
    }
}
