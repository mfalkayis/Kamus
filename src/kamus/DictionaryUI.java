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

    // --- TEMA MOCCA & MONOSPACE ---
    private final String FONT_NAME = "DejaVu Sans Mono"; 
    private final Color COLOR_HEADER = new Color(205, 185, 160); 
    private final Color COLOR_BG = new Color(245, 240, 235);     
    private final Color COLOR_BTN_SEARCH = new Color(220, 75, 65); 
    private final Color COLOR_BTN_HOME = new Color(90, 90, 90); 
    private final Color COLOR_TEXT = Color.BLACK;
    
    public DictionaryUI() {
        dictionaryManager = new DictionaryManager();
        dictionaryManager.initializeDictionary();

        setTitle("Kamus"); 
        setSize(900, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initHeader();
        initContentArea();
        
        resetView();
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); 
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(new EmptyBorder(20, 40, 30, 40)); 

        // 1. Judul Aplikasi
        JLabel lblAppTitle = new JLabel("KAMUS");
        lblAppTitle.setFont(new Font(FONT_NAME, Font.BOLD, 32));
        lblAppTitle.setForeground(Color.BLACK);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAppTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblAppTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetView();
            }
        });
        
        // 2. Panel Pencarian
        JPanel searchContainer = new JPanel(new BorderLayout(5, 0));
        searchContainer.setBackground(COLOR_HEADER);
        searchContainer.setMaximumSize(new Dimension(800, 45)); 
        
        JButton btnHome = new JButton("⌂"); 
        btnHome.setBackground(COLOR_BTN_HOME);
        btnHome.setForeground(Color.WHITE);
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 18)); 
        btnHome.setFocusPainted(false);
        btnHome.setBorder(new EmptyBorder(0, 15, 0, 15));
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setToolTipText("Kembali ke Awal (Reset)");
        btnHome.addActionListener(e -> resetView());

        txtSearch = new JTextField();
        txtSearch.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        txtSearch.setBorder(new EmptyBorder(5, 10, 5, 10)); 
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

        searchContainer.add(btnHome, BorderLayout.WEST);
        searchContainer.add(txtSearch, BorderLayout.CENTER);
        searchContainer.add(btnSearch, BorderLayout.EAST);
        
        // 3. Toggle
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        togglePanel.setBackground(COLOR_HEADER);
        
        btnSwitch = new JButton("Indonesia");
        btnSwitch.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        btnSwitch.setBackground(COLOR_HEADER); 
        btnSwitch.setForeground(Color.BLACK);
        btnSwitch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1), 
                new EmptyBorder(5, 40, 5, 40)   
        ));
        btnSwitch.setFocusPainted(false);
        btnSwitch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSwitch.addActionListener(e -> switchIndex());
        
        togglePanel.add(btnSwitch);

        headerPanel.add(lblAppTitle);
        headerPanel.add(Box.createVerticalStrut(15)); 
        headerPanel.add(searchContainer);
        headerPanel.add(Box.createVerticalStrut(10)); 
        headerPanel.add(togglePanel);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initContentArea() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); 

        lblResultTitle = new JLabel();
        lblResultTitle.setFont(new Font(FONT_NAME, Font.BOLD, 42)); 
        lblResultTitle.setForeground(new Color(40, 50, 70)); 
        lblResultTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblResultCategory = new JLabel();
        lblResultCategory.setFont(new Font(FONT_NAME, Font.ITALIC, 16));
        lblResultCategory.setForeground(Color.GRAY);
        lblResultCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtResultDesc = new JTextArea();
        txtResultDesc.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        txtResultDesc.setForeground(COLOR_TEXT);
        txtResultDesc.setLineWrap(true);
        txtResultDesc.setWrapStyleWord(true);
        txtResultDesc.setEditable(false);
        txtResultDesc.setBackground(COLOR_BG);
        txtResultDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtResultDesc.setBorder(new EmptyBorder(20, 0, 0, 0)); 

        contentPanel.add(lblResultTitle);
        contentPanel.add(lblResultCategory);
        contentPanel.add(txtResultDesc);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    // --- RESET VIEW (HOME) ---
    private void resetView() {
        lblResultTitle.setText("Wiki");
        lblResultCategory.setText("Ensiklopedia Kamus");
        
        txtResultDesc.setText(
            "Selamat datang di Aplikasi Kamus "
        );

        txtSearch.setText(""); 
        txtSearch.requestFocus(); 
    }

    private void switchIndex() {
        isIndoIndex = !isIndoIndex;
        if (isIndoIndex) {
            btnSwitch.setText("Indonesia");
        } else {
            btnSwitch.setText("Inggris");
        }
        resetView(); 
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

            // Multimedia
            String mediaKey = isIndoIndex ? result.getIndoWord() : result.getEnglishWord();
            boolean videoPlayed = playVideo(mediaKey);
            if (!videoPlayed) {
                showImagePopup(mediaKey);
                playAudio(mediaKey);
            }
        } else {
            lblResultTitle.setText("404");
            lblResultCategory.setText("Tidak Ditemukan");
            txtResultDesc.setText("Maaf, kata '" + keyword + "' tidak ditemukan dalam database.");
        }
    }

    // --- LOGIKA MULTIMEDIA  ---
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