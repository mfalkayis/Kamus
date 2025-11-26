package com.kamus.core.models;

// Enumerasi untuk warna Red-Black Tree
enum Color {
    RED, BLACK
}

public class Node {
    public WordEntry data; // Data kamus yang disimpan
    public String key;     // Kunci pencarian (kita gunakan WordEntry.indoWord)

    public Node parent;
    public Node left;
    public Node right;
    public Color color;

    // Konstruktor untuk Node baru
    public Node(WordEntry data) {
        this.data = data;
        this.key = data.getIndoWord(); // Kunci utamanya adalah Kata Indonesia

        // Node baru selalu disisipkan sebagai RED (sesuai aturan RBT)
        this.color = Color.RED; 
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public String getKey() {
        return key;
    }
}