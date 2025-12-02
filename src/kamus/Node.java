package kamus;

enum RBTColor { RED, BLACK }

public class Node {
    public WordEntry data;
    public String key; 
    public Node parent, left, right;
    public RBTColor color;

    private Gimmick gimmick; 

    public Node(WordEntry data) {
        this.data = data;
        if (data != null) {
            this.key = data.getIndoWord();
            this.gimmick = GimmickStrategy.create(data.getIndoWord());
        }
        
        this.color = RBTColor.RED;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public String getKey() { return key; }

    public void runGimmick(DictionaryUI ui) {
        if (this.gimmick != null) {
            this.gimmick.execute(ui);
        }
    }
}