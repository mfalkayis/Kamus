package com.kamus.core.dictionary;

import com.kamus.core.models.Node;
import com.kamus.core.models.WordEntry;
// Perlu mengimpor Color jika didefinisikan secara terpisah dari Node.java
// Anggap Color didefinisikan sebagai enum di Node.java atau di-import.
// Kita akan mendefinisikan placeholder untuk null Node (NIL/sentinel)
// karena Red-Black Tree memerlukannya.

// Catatan: Asumsikan enum Color diakses melalui package-private atau diimport
// Jika Color ada di Node.java: class Node { enum Color {RED, BLACK} ...}
// Kita akan membuat Node.NIL (sentinel) di sini.

public class RedBlackTree {
    private Node root;
    
    // Sentinel Node (NIL) - semua leaf dan root parent akan menunjuk ke sini
    // NIL selalu berwarna BLACK
    public static final Node NIL = new Node(new WordEntry("", "", "", ""));
    static {
        NIL.color = com.kamus.core.models.Color.BLACK;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL; // Untuk keamanan, meskipun parent NIL jarang dipakai
    }
    
    public RedBlackTree() {
        // Inisialisasi root sebagai NIL saat tree kosong
        root = NIL; 
    }

    // Getter untuk root (berguna untuk traversals atau debugging)
    public Node getRoot() {
        return root;
    }
    
    // --- Placeholder Metode RBT ---
    
    public void insert(WordEntry entry) {
        Node newNode = new Node(entry);
        newNode.left = NIL;
        newNode.right = NIL;
        
        // 1. Lakukan Binary Search Tree Insertion
        Node y = NIL;
        Node x = this.root;
        
        while (x != NIL) {
            y = x;
            // Gunakan .compareTo untuk perbandingan String
            if (newNode.getKey().compareTo(x.getKey()) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        
        newNode.parent = y;
        if (y == NIL) {
            root = newNode; // Tree kosong
        } else if (newNode.getKey().compareTo(y.getKey()) < 0) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }
        
        // Jika Node baru adalah root, ia harus BLACK
        if (newNode.parent == NIL) {
            newNode.color = com.kamus.core.models.Color.BLACK;
            return;
        }

        // Jika parent root, tidak perlu fixup
        if (newNode.parent.parent == NIL) {
            return;
        }
        
        // 2. Perbaiki (fix) pelanggaran RBT
        insertFixUp(newNode); 
    }
    
    // Method kosong, akan diisi pada langkah selanjutnya
    private void insertFixUp(Node k) {
        // Akan diimplementasikan Logika Rotasi dan Rekolorasi RBT
    }
    
    // Method placeholder untuk rotasi (akan diisi pada langkah selanjutnya)
    private void leftRotate(Node x) {
        Node y = x.right; // y adalah right child dari x
        x.right = y.left; // Subtree kiri y menjadi subtree kanan x

        if (y.left != NIL) {
            y.left.parent = x;
        }

        y.parent = x.parent; // Parent x menjadi parent y

        if (x.parent == NIL) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x; // x menjadi left child dari y
        x.parent = y;
    }
    
    private void rightRotate(Node x) {
        Node y = x.left; // y adalah left child dari x
        x.left = y.right; // Subtree kanan y menjadi subtree kiri x

        if (y.right != NIL) {
            y.right.parent = x;
        }

        y.parent = x.parent; // Parent x menjadi parent y

        if (x.parent == NIL) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x; // x menjadi right child dari y
        x.parent = y;
    }
    
    public WordEntry search(String key) {
        Node current = root;
        while (current != NIL) {
            if (key.equalsIgnoreCase(current.getKey())) {
                return current.data; // Ditemukan
            }
            if (key.compareToIgnoreCase(current.getKey()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null; // Tidak ditemukan
    }
}