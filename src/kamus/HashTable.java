package kamus;

import java.util.HashMap;

public class HashTable {
    private HashMap<String, WordEntry> reverseMap;

    public HashTable() {
        this.reverseMap = new HashMap<>();
    }

    public void insert(WordEntry entry) {
        this.reverseMap.put(entry.getEnglishWord().toLowerCase(), entry);
    }

    public int size() {
        return reverseMap.size();
    }

    // --- VISUALISASI SEARCH HASHING ---
    public WordEntry search(String englishKey) {
        String keyLower = englishKey.toLowerCase();
        
        System.out.println("\n================================================");
        System.out.println("#️⃣  [HASH TABLE SEARCH] Mencari Kata: \"" + englishKey + "\"");
        System.out.println("================================================");

        // 1. Tunjukkan Rumus Hash
        int hashCode = keyLower.hashCode();
        // Simulasi index bucket (biasanya hashcode % ukuran array, tapi di HashMap Java internal)
        int bucketIndex = Math.abs(hashCode) % 16; 
        
        System.out.println("   1. Menghitung Hash Code (Rumus Matematis):");
        System.out.println("      Key: \"" + keyLower + "\" -> Hash: " + hashCode);
        System.out.println("   2. Memetakan ke Bucket (Loker) Memori:");
        System.out.println("      Target Bucket Index ~ " + bucketIndex);
        
        // 2. Akses Langsung
        long start = System.nanoTime();
        WordEntry result = this.reverseMap.get(keyLower);
        long end = System.nanoTime();

        if (result != null) {
            System.out.println("   3. DITEMUKAN SECARA INSTAN! (O(1))");
            System.out.println("      Waktu akses memori: " + (end - start) + " nanodetik");
        } else {
            System.out.println("   3. Tidak ditemukan di bucket manapun.");
        }
        System.out.println("================================================\n");

        return result;
    }
}