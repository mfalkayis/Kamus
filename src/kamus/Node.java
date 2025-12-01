package kamus;

enum RBTColor {
    RED, BLACK
}

public class Node {
    public WordEntry data;
    public String key; 
    
    public Node parent;
    public Node left;
    public Node right;
    public RBTColor color; 

    public Node(WordEntry data) {
        this.data = data;
        if (data != null) {
            this.key = data.getIndoWord();
        }
        this.color = RBTColor.RED; 
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public String getKey() {
        return key;
    }
}