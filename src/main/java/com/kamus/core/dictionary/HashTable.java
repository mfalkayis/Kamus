package com.kamus.core.dictionary;

import com.kamus.core.models.WordEntry;
import java.util.HashMap;

public class HashTable {
    // Kunci: Kata Inggris, Nilai: Objek WordEntry (termasuk kata Indonesia)
    private HashMap<String, WordEntry> reverseMap;

    public HashTable() {
        this.reverseMap = new HashMap<>();
    }

    // Method untuk menyisipkan data (kunci: Kata Inggris)
    public void insert(WordEntry entry) {
        // Menggunakan toLowerCase() agar pencarian tidak peka huruf besar/kecil
        this.reverseMap.put(entry.getEnglishWord().toLowerCase(), entry);
    }

    // Method untuk pencarian (Inggris -> Indonesia)
    public WordEntry search(String englishKey) {
        // Cari dengan kunci yang sudah diubah ke huruf kecil
        return this.reverseMap.get(englishKey.toLowerCase());
    }
    
    // Method untuk mengetahui ukuran
    public int size() {
        return reverseMap.size();
    }
}