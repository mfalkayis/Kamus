package com.kamus.core.dictionary;

import com.kamus.core.models.WordEntry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {

    private final String DATA_FILE_PATH = "res/data/dictionary_data.txt";
    private static final String CSV_DELIMITER = ",";

    public List<WordEntry> loadDictionaryData() {
        List<WordEntry> entries = new ArrayList<>();
        
        // Menggunakan try-with-resources untuk memastikan BufferedReader tertutup
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Pastikan baris tidak kosong sebelum diproses
                if (line.trim().isEmpty()) continue; 
                
                // Pisahkan baris menggunakan delimiter
                String[] parts = line.split(CSV_DELIMITER, 4); 

                // Validasi jumlah bagian
                if (parts.length == 4) {
                    // Trim untuk menghilangkan spasi yang tidak perlu
                    String indoWord = parts[0].trim();
                    String englishWord = parts[1].trim();
                    String wordType = parts[2].trim();
                    String description = parts[3].trim();
                    
                    // Buat objek WordEntry dan tambahkan ke list
                    entries.add(new WordEntry(indoWord, englishWord, wordType, description));
                } else {
                    System.err.println("Error: Baris data tidak valid: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat membaca file data kamus: " + e.getMessage());
            e.printStackTrace();
        }
        return entries;
    }
}