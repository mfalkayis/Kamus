package kamus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    private final String DATA_FILE_PATH = "data/dictionary.txt";
    private static final String CSV_DELIMITER = ",";

    private RedBlackTree indoToEnglishTree;
    private HashTable englishToIndoTable;

    public DictionaryManager() {
        this.indoToEnglishTree = new RedBlackTree();
        this.englishToIndoTable = new HashTable();
    }

    public void initializeDictionary() {
        List<WordEntry> entries = loadDictionaryData();
        for (WordEntry entry : entries) {
            // Kita buat Node secara manual disini agar bisa dishare
            // Tapi karena RBT.insert biasanya membuat Node baru di dalam, 
            // kita perlu memodifikasi RBT.insert sedikit ATAU kita biarkan RBT membuat node, 
            // lalu kita cari node itu untuk dimasukkan ke Hash.
            
            // Biarkan RBT membuat Node, lalu kita ambil Node itu (search) untuk dimasukkan ke Hash.
            this.indoToEnglishTree.insert(entry);
            
            // Cari node yang baru saja dibuat
            Node createdNode = this.indoToEnglishTree.search(entry.getIndoWord());
            
            // Masukkan Node yang SAMA ke Hash Table
            if (createdNode != null) {
                this.englishToIndoTable.insert(createdNode);
            }
        }
        this.indoToEnglishTree.printTree();
    }

    // Return Node sekarang
    public Node searchIndoToEnglish(String key) {
        return indoToEnglishTree.search(key);
    }

    public Node searchEnglishToIndo(String key) {
        return englishToIndoTable.search(key);
    }
    
    // ... method loadDictionaryData  ...
    private List<WordEntry> loadDictionaryData() {
        List<WordEntry> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(CSV_DELIMITER, 4);
                if (parts.length == 4) {
                    entries.add(new WordEntry(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return entries;
    }
}