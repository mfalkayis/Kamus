package com.kamus.core.models;

public class WordEntry {
    // Properti sesuai format CSV: Indo, Inggris, Jenis, Deskripsi
    private String indoWord;
    private String englishWord;
    private String wordType;
    private String description;

    public WordEntry(String indoWord, String englishWord, String wordType, String description) {
        this.indoWord = indoWord;
        this.englishWord = englishWord;
        this.wordType = wordType;
        this.description = description;
    }

    // --- Getter Methods ---

    public String getIndoWord() {
        return indoWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getWordType() {
        return wordType;
    }

    public String getDescription() {
        return description;
    }

    // Method to display the entry
    @Override
    public String toString() {
        return "ID: " + indoWord + " | EN: " + englishWord + " (" + wordType + ")";
    }
}