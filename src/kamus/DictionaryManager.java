package kamus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    // PATH RELATIF KE FOLDER DATA
    private final String DATA_FILE_PATH = "data/dictionary.txt";
    private static final String CSV_DELIMITER = ",";

    private RedBlackTree indoToEnglishTree;
    private HashTable englishToIndoTable;

    public DictionaryManager() {
        this.indoToEnglishTree = new RedBlackTree();
        this.englishToIndoTable = new HashTable();
    }

    public void initializeDictionary() {
        System.out.println("Memulai loading data...");
        List<WordEntry> entries = loadDictionaryData();

        for (WordEntry entry : entries) {
            // 1. Masukkan ke RBT (Indo -> Inggris)
            this.indoToEnglishTree.insert(entry);
            // 2. Masukkan ke Hash Table (Inggris -> Indo)
            this.englishToIndoTable.insert(entry);
        }

        this.indoToEnglishTree.printTree(); 
        
        System.out.println("Data Loaded. Total Kata: " + entries.size());
    }

    public WordEntry searchIndoToEnglish(String indoKey) {
        return indoToEnglishTree.search(indoKey);
    }

    public WordEntry searchEnglishToIndo(String englishKey) {
        return englishToIndoTable.search(englishKey);
    }

    private List<WordEntry> loadDictionaryData() {
        List<WordEntry> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(CSV_DELIMITER, 4);
                if (parts.length == 4) {
                    entries.add(new WordEntry(
                        parts[0].trim(), parts[1].trim(), 
                        parts[2].trim(), parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file data: " + e.getMessage());
            System.err.println("Pastikan file ada di folder: " + DATA_FILE_PATH);
        }
        return entries;
    }
}