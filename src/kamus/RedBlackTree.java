package kamus;

public class RedBlackTree {
    private Node root;
    public static final Node NIL = new Node(null); 
    static {
        NIL.color = RBTColor.BLACK;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }
    
    public RedBlackTree() { root = NIL; }
    public Node getRoot() { return root; }

    public void insert(WordEntry entry) {
        // Log Visualisasi Insert (Kita print simpel saja biar terminal tidak penuh saat loading awal)
        // System.out.println("[+] Menyisipkan ke RBT: " + entry.getIndoWord());

        Node node = new Node(entry);
        node.left = NIL;
        node.right = NIL;
        Node y = NIL;
        Node x = this.root;

        while (x != NIL) {
            y = x;
            if (node.getKey().compareToIgnoreCase(x.getKey()) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == NIL) {
            root = node;
        } else if (node.getKey().compareToIgnoreCase(y.getKey()) < 0) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == NIL) {
            node.color = RBTColor.BLACK;
            return;
        }
        if (node.parent.parent == NIL) return;

        insertFixUp(node);
    }

    private void insertFixUp(Node k) {
        while (k.parent.color == RBTColor.RED) {
            Node u;
            if (k.parent == k.parent.parent.left) {
                u = k.parent.parent.right;
                if (u.color == RBTColor.RED) {
                    k.parent.color = RBTColor.BLACK;
                    u.color = RBTColor.BLACK;
                    k.parent.parent.color = RBTColor.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k); // Visualisasi Rotasi ada di method leftRotate
                    }
                    k.parent.color = RBTColor.BLACK;
                    k.parent.parent.color = RBTColor.RED;
                    rightRotate(k.parent.parent); // Visualisasi Rotasi
                }
            } else {
                u = k.parent.parent.left;
                if (u.color == RBTColor.RED) {
                    k.parent.color = RBTColor.BLACK;
                    u.color = RBTColor.BLACK;
                    k.parent.parent.color = RBTColor.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = RBTColor.BLACK;
                    k.parent.parent.color = RBTColor.RED;
                    leftRotate(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        root.color = RBTColor.BLACK;
    }

    private void leftRotate(Node x) {
        System.out.println("   [ROTASI] Kiri pada node: " + x.getKey());
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) this.root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        System.out.println("   [ROTASI] Kanan pada node: " + x.getKey());
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) this.root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    // --- VISUALISASI PENCARIAN (SEARCH) ---
    public WordEntry search(String key) {
        System.out.println("\n------------------------------------------------");
        System.out.println("[RBT SEARCH] Mencari Kata: \"" + key + "\"");
        System.out.println("------------------------------------------------");
        
        Node current = root;
        int steps = 0;

        while (current != NIL) {
            steps++;
            int cmp = key.compareToIgnoreCase(current.getKey());
            
            // Mengganti Emot Warna dengan Teks
            String nodeColor = (current.color == RBTColor.RED) ? "[MERAH]" : "[HITAM]";
            System.out.println("   Langkah " + steps + ": Cek Node [" + current.getKey() + "] " + nodeColor);

            if (cmp == 0) {
                System.out.println("   [KETEMU] Data ditemukan pada kedalaman: " + steps);
                System.out.println("------------------------------------------------\n");
                return current.data;
            } else if (cmp < 0) {
                System.out.println("      >> Kata lebih kecil (A-Z), belok ke KIRI (L)");
                current = current.left;
            } else {
                System.out.println("      >> Kata lebih besar (A-Z), belok ke KANAN (R)");
                current = current.right;
            }
        }
        System.out.println("   [X] TIDAK KETEMU. Sampai di ujung daun (NIL).");
        System.out.println("------------------------------------------------\n");
        return null;
    }

    public void printTree() {
        System.out.println("\n[VISUALISASI STRUKTUR RED-BLACK TREE]");
        System.out.println("========================================");
        if (root == NIL) {
            System.out.println("(Pohon Kosong)");
        } else {
            printTreeRecursive(root, "", true, "(ROOT)");
        }
        System.out.println("========================================\n");
    }

    // Method rekursif untuk menggambar garis cabang
    private void printTreeRecursive(Node node, String indent, boolean isLast, String position) {
        if (node != NIL) {
            System.out.print(indent);
            
            // Simbol cabang menggunakan karakter ASCII standar yang lebih aman
            if (isLast) {
                System.out.print("L-- "); // L-- menggantikan simbol siku
                indent += "    ";
            } else {
                System.out.print("|-- "); // |-- menggantikan simbol cabang
                indent += "|   ";
            }

            // Indikator Warna (Ganti Emotikon dengan Teks)
            String colorIcon = (node.color == RBTColor.RED) ? "(R)" : "(B)";
            
            // Print Data: [Posisi] Kata (Warna)
            // (R) = RED/MERAH, (B) = BLACK/HITAM
            System.out.println(position + " " + node.getKey() + " " + colorIcon);

            printTreeRecursive(node.left, indent, false, "[L]");
            printTreeRecursive(node.right, indent, true,  "[R]");
        }
    }
}