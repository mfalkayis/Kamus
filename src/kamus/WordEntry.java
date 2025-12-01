package kamus;

public class WordEntry {
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

    public String getIndoWord() { return indoWord; }
    public String getEnglishWord() { return englishWord; }
    public String getWordType() { return wordType; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return indoWord + " -> " + englishWord;
    }
}