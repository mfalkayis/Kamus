package kamus;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DictionaryUI app = new DictionaryUI();
            app.setVisible(true);
        });
    }
}