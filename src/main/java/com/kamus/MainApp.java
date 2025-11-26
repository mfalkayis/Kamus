package com.kamus;

import com.kamus.ui.DictionaryUI;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Menjalankan UI di Event Dispatch Thread (Standard Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                // Opsional: Mencoba menggunakan style native sistem operasi jika ada
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            DictionaryUI app = new DictionaryUI();
            app.setVisible(true);
        });
    }
}
