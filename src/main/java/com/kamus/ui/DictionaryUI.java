package com.kamus.ui;

import com.kamus.core.dictionary.DictionaryManager;
import com.kamus.core.models.WordEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DictionaryUI extends JFrame {

    private DictionaryManager dictionaryManager;
    private boolean isIndoToEng = true; // Status arah terjemahan

    // Komponen UI
    private JLabel lblSourceLang;
    private JLabel lblTargetLang;
    private JTextArea txtInput;
    private JTextArea txtResult;
    private JLabel lblImagePlaceholder; // Tempat untuk Gimmick Gambar nanti
    private JButton btnSearch;

    // Warna Tema (Dark Mode Sederhana)
    private final Color COLOR_BG = new Color(40, 44, 52);
    private final Color COLOR_PANEL = new Color(33, 37, 43);
    private final Color COLOR_TEXT = new Color(220, 223, 228);
    private final Color COLOR_ACCENT = new Color(97, 175, 239); // Biru muda

    public DictionaryUI() {
        // 1. Inisialisasi Backend
        dictionaryManager = new DictionaryManager();
        dictionaryManager.initializeDictionary();

        // 2. Setup Window Utama
        setTitle("Kamus Struktur Data (Red-Black Tree)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengah layar
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // 3. Inisialisasi Komponen
        initTopPanel();
        initCenterPanel();
        initBottomPanel();
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(COLOR_BG);

        lblSourceLang = new JLabel("Indonesia");
        lblSourceLang.setForeground(COLOR_ACCENT);
        lblSourceLang.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnSwitch = new JButton("⇄");
        btnSwitch.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnSwitch.setFocusPainted(false);
        btnSwitch.setBackground(COLOR_PANEL);
        btnSwitch.setForeground(Color.WHITE);
        btnSwitch.addActionListener(e -> switchLanguage());

        lblTargetLang = new JLabel("Inggris");
        lblTargetLang.setForeground(COLOR_ACCENT);
        lblTargetLang.setFont(new Font("SansSerif", Font.BOLD, 16));

        topPanel.add(lblSourceLang);
        topPanel.add(btnSwitch);
        topPanel.add(lblTargetLang);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        centerPanel.setBackground(COLOR_BG);

        // --- Kolom Kiri (Input) ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COLOR_PANEL);
        leftPanel.setBorder(BorderFactory.createTitledBorder(null, "Masukan Teks", 0, 0, null, Color.GRAY));

        txtInput = new JTextArea();
        txtInput.setBackground(COLOR_PANEL);
        txtInput.setForeground(COLOR_TEXT);
        txtInput.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtInput.setLineWrap(true);
        txtInput.setCaretColor(Color.WHITE);
        
        // Fitur: Tekan Enter untuk mencari
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Mencegah enter membuat baris baru
                    performSearch();
                }
            }
        });

        leftPanel.add(new JScrollPane(txtInput), BorderLayout.CENTER);

        // --- Kolom Kanan (Hasil + Gimmick) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(COLOR_PANEL);
        rightPanel.setBorder(BorderFactory.createTitledBorder(null, "Terjemahan", 0, 0, null, Color.GRAY));

        txtResult = new JTextArea();
        txtResult.setBackground(COLOR_PANEL);
        txtResult.setForeground(COLOR_TEXT);
        txtResult.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtResult.setLineWrap(true);
        txtResult.setEditable(false); // Hasil tidak boleh diedit user

        // Placeholder untuk Gambar (Gimmick)
        lblImagePlaceholder = new JLabel(" [Area Gambar/Video] ", SwingConstants.CENTER);
        lblImagePlaceholder.setForeground(Color.GRAY);
        lblImagePlaceholder.setPreferredSize(new Dimension(100, 150));
        lblImagePlaceholder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        rightPanel.add(new JScrollPane(txtResult), BorderLayout.CENTER);
        rightPanel.add(lblImagePlaceholder, BorderLayout.SOUTH); // Gambar di bawah teks

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(COLOR_BG);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnSearch = new JButton("Terjemahkan");
        btnSearch.setPreferredSize(new Dimension(150, 40));
        btnSearch.setBackground(COLOR_ACCENT);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setFocusPainted(false);
        
        btnSearch.addActionListener(e -> performSearch());

        bottomPanel.add(btnSearch);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Logika Aplikasi ---

    private void switchLanguage() {
        isIndoToEng = !isIndoToEng; // Toggle boolean
        
        if (isIndoToEng) {
            lblSourceLang.setText("Indonesia");
            lblTargetLang.setText("Inggris");
        } else {
            lblSourceLang.setText("Inggris");
            lblTargetLang.setText("Indonesia");
        }
        
        // Bersihkan area saat ditukar
        txtInput.setText("");
        txtResult.setText("");
        lblImagePlaceholder.setIcon(null);
        lblImagePlaceholder.setText(" [Area Gambar/Video] ");
    }

    private void performSearch() {
        String keyword = txtInput.getText().trim();
        if (keyword.isEmpty()) return;

        WordEntry result = null;
        long startTime = System.nanoTime(); // Hitung waktu proses (opsional)

        if (isIndoToEng) {
            // Gunakan Red-Black Tree
            result = dictionaryManager.searchIndoToEnglish(keyword);
        } else {
            // Gunakan Hash Table (Reverse Lookup)
            result = dictionaryManager.searchEnglishToIndo(keyword);
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0; // milidetik

        displayResult(result, duration);
    }

    private void displayResult(WordEntry result, double duration) {
        if (result != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(isIndoToEng ? result.getEnglishWord() : result.getIndoWord());
            sb.append("\n\n");
            sb.append("Jenis: ").append(result.getWordType()).append("\n");
            sb.append("Deskripsi: ").append(result.getDescription()).append("\n");
            sb.append("\n----------------\n");
            sb.append("Waktu pencarian: ").append(duration).append(" ms");
            
            txtResult.setText(sb.toString());
            
            // TODO: Di sini nanti kita akan memanggil fungsi Gimmick (Gambar/Video)
            lblImagePlaceholder.setText("[Gambar ditemukan]");
            
        } else {
            txtResult.setText("Kata tidak ditemukan dalam kamus.");
            lblImagePlaceholder.setText(" [?] ");
        }
    }
}