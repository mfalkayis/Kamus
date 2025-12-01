package kamus;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DictionaryUI extends JFrame {

    private DictionaryManager dictionaryManager;
    private boolean isIndoIndex = true;

    // Komponen UI
    private JTextField txtSearch;
    private JLabel lblResultTitle;
    private JLabel lblResultCategory;
    private JTextArea txtResultDesc;
    private JButton btnSwitch;
    
    // Multimedia
    private JWindow currentPopup; 
    private Timer popupTimer;     

    // --- TEMA BARU (MOCCA & MONOSPACE) ---
    // Font Utama
    private final String FONT_NAME = "DejaVu Sans Mono"; 

    // Palet Warna (Disesuaikan dengan Screenshot)
    private final Color COLOR_HEADER = new Color(205, 185, 160); // Warna Mocca/Latte
    private final Color COLOR_BG = new Color(245, 240, 235);     // Cream/Putih Tulang
    private final Color COLOR_BTN_SEARCH = new Color(220, 75, 65); // Merah Bata
    private final Color COLOR_TEXT = Color.BLACK;
    
    public DictionaryUI() {
        dictionaryManager = new DictionaryManager();
        dictionaryManager.initializeDictionary();

        setTitle("Kamus"); // Judul Window Simpel
        // UKURAN BARU: Lebih lebar (Landscape) sesuai screenshot ke-2
        setSize(900, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initHeader();
        initContentArea();
    }

    private void initHeader() {
        // Panel Header Utama (Warna Mocca)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Susun vertikal
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(new EmptyBorder(20, 40, 30, 40)); // Padding

        // 1. Judul "KAMUS" Besar
        JLabel lblAppTitle = new JLabel("KAMUS");
        lblAppTitle.setFont(new Font(FONT_NAME, Font.BOLD, 32));
        lblAppTitle.setForeground(Color.BLACK);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 2. Panel Pencarian (Search Bar + Tombol Merah)
        JPanel searchContainer = new JPanel(new BorderLayout(5, 0));
        searchContainer.setBackground(COLOR_HEADER);
        searchContainer.setMaximumSize(new Dimension(800, 45)); // Batasi lebar agar rapi
        
        txtSearch = new JTextField();
        txtSearch.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        txtSearch.setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding teks
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performSearch();
            }
        });

        JButton btnSearch = new JButton("Cari");
        btnSearch.setBackground(COLOR_BTN_SEARCH);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(new EmptyBorder(0, 20, 0, 20));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> performSearch());

        searchContainer.add(txtSearch, BorderLayout.CENTER);
        searchContainer.add(btnSearch, BorderLayout.EAST);
        
        // 3. Tombol Toggle (Mode Bahasa) di bawah Search Bar
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        togglePanel.setBackground(COLOR_HEADER);
        
        btnSwitch = new JButton("Indonesia");
        btnSwitch.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        btnSwitch.setBackground(COLOR_HEADER); // Warna sama dengan header
        btnSwitch.setForeground(Color.BLACK);
        btnSwitch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1), // Garis pinggir hitam tipis
                new EmptyBorder(5, 40, 5, 40)   // Padding dalam lebar
        ));
        btnSwitch.setFocusPainted(false);
        btnSwitch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSwitch.addActionListener(e -> switchIndex());
        
        togglePanel.add(btnSwitch);

        // Menambahkan komponen ke Header Panel dengan spasi
        headerPanel.add(lblAppTitle);
        headerPanel.add(Box.createVerticalStrut(15)); // Jarak judul ke search
        headerPanel.add(searchContainer);
        headerPanel.add(Box.createVerticalStrut(10)); // Jarak search ke toggle
        headerPanel.add(togglePanel);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initContentArea() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); // Margin kiri-kanan lebih luas

        // 1. Judul Kata (Besar, Font Monospace)
        lblResultTitle = new JLabel("Wiki Pintar");
        lblResultTitle.setFont(new Font(FONT_NAME, Font.BOLD, 42)); // Font Besar
        lblResultTitle.setForeground(new Color(40, 50, 70)); // Biru Gelap hampir hitam
        lblResultTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. Sub-judul / Terjemahan (Miring)
        lblResultCategory = new JLabel("Ketik kata di kolom pencarian...");
        lblResultCategory.setFont(new Font(FONT_NAME, Font.ITALIC, 16));
        lblResultCategory.setForeground(Color.GRAY);
        lblResultCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 3. Deskripsi
        txtResultDesc = new JTextArea();
        txtResultDesc.setText("");
        txtResultDesc.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        txtResultDesc.setForeground(COLOR_TEXT);
        txtResultDesc.setLineWrap(true);
        txtResultDesc.setWrapStyleWord(true);
        txtResultDesc.setEditable(false);
        txtResultDesc.setBackground(COLOR_BG);
        txtResultDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtResultDesc.setBorder(new EmptyBorder(20, 0, 0, 0)); // Jarak dari judul

        contentPanel.add(lblResultTitle);
        contentPanel.add(lblResultCategory);
        contentPanel.add(txtResultDesc);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private void switchIndex() {
        isIndoIndex = !isIndoIndex;
        if (isIndoIndex) {
            btnSwitch.setText("Indonesia");
        } else {
            btnSwitch.setText("Inggris / Alias");
        }
        txtSearch.setText("");
        txtSearch.requestFocus();
    }

    private void performSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) return;

        WordEntry result;
        if (isIndoIndex) {
            result = dictionaryManager.searchIndoToEnglish(keyword);
        } else {
            result = dictionaryManager.searchEnglishToIndo(keyword);
        }

        if (result != null) {
            String title = isIndoIndex ? result.getIndoWord() : result.getEnglishWord();
            String subtitle = isIndoIndex ? result.getEnglishWord() : result.getIndoWord();
            
            lblResultTitle.setText(title);
            lblResultCategory.setText(subtitle + " • " + result.getWordType());
            txtResultDesc.setText(result.getDescription());

            // --- MULTIMEDIA ---
            String mediaKey = isIndoIndex ? result.getIndoWord() : result.getEnglishWord();
            
            boolean videoPlayed = playVideo(mediaKey);
            if (!videoPlayed) {
                showImagePopup(mediaKey);
                playAudio(mediaKey);
            }
        } else {
            lblResultTitle.setText("404");
            lblResultCategory.setText("Tidak Ditemukan");
            txtResultDesc.setText("Maaf, kata '" + keyword + "' tidak ditemukan.");
        }
    }

    // --- LOGIKA MULTIMEDIA (TIDAK BERUBAH) ---

    private boolean playVideo(String keyword) {
        String videoPath = "data/video/" + keyword.toLowerCase() + ".mp4";
        File videoFile = new File(videoPath);
        if (videoFile.exists()) {
            new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(videoFile);
                } catch (IOException e) { e.printStackTrace(); }
            }).start();
            return true;
        }
        return false;
    }

    private void playAudio(String keyword) {
        new Thread(() -> {
            try {
                String soundPath = "data/audio/" + keyword.toLowerCase() + ".wav";
                File soundFile = new File(soundPath);
                if (soundFile.exists()) {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            } catch (Exception e) {}
        }).start();
    }

    private void showImagePopup(String keyword) {
        if (currentPopup != null) {
            currentPopup.dispose();
            if (popupTimer != null) popupTimer.stop();
        }
        String basePath = "data/images/" + keyword.toLowerCase();
        File imgFile = new File(basePath + ".jpg");
        if (!imgFile.exists()) imgFile = new File(basePath + ".png");
        if (!imgFile.exists()) return;

        currentPopup = new JWindow(this);
        try { currentPopup.setBackground(new Color(0, 0, 0, 0)); } 
        catch (UnsupportedOperationException e) { currentPopup.setBackground(COLOR_BG); }

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(null);

        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
        Image img = icon.getImage();
        int maxW = 350;
        Image newImg = img.getScaledInstance(maxW, -1, Image.SCALE_SMOOTH); 

        JLabel lblImg = new JLabel(new ImageIcon(newImg));
        JLabel lblCaption = new JLabel(keyword, SwingConstants.CENTER);
        lblCaption.setForeground(Color.BLACK);
        lblCaption.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        lblCaption.setOpaque(true);
        lblCaption.setBackground(new Color(255, 255, 255, 200));
        lblCaption.setBorder(new EmptyBorder(5, 10, 5, 10));

        content.add(lblImg, BorderLayout.CENTER);
        content.add(lblCaption, BorderLayout.SOUTH);
        currentPopup.add(content);

        currentPopup.pack();
        currentPopup.setLocationRelativeTo(null);
        currentPopup.setVisible(true);
        currentPopup.setAlwaysOnTop(true);

        popupTimer = new Timer(3000, e -> {
            if (currentPopup != null) {
                currentPopup.dispose();
                currentPopup = null;
            }
        });
        popupTimer.setRepeats(false);
        popupTimer.start();
    }
}