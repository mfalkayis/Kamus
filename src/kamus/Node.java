package kamus;

// UBAH NAMA DISINI DARI Color JADI RBTColor
enum RBTColor {
    RED, BLACK
}

public class Node {
    public WordEntry data;
    public String key; 
    
    public Node parent;
    public Node left;
    public Node right;
    public RBTColor color; // UBAH TIPE DATA DISINI

    public Node(WordEntry data) {
        this.data = data;
        if (data != null) {
            this.key = data.getIndoWord();
        }
        this.color = RBTColor.RED; // UBAH DISINI
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public String getKey() {
        return key;
    }
}