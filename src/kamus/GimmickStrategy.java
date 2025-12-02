package kamus;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class GimmickStrategy {

    public static Gimmick create(String keyword) {
        String key = keyword.toLowerCase();

        // --- 1. CEK GIMMICK KODE ---
        if (key.contains("miring") || key.contains("italic")) {
            return new ItalicTextGimmick();
        }
        if (key.contains("kabur") || key.contains("blur")) {
            return new BlurScreenGimmick();
        }
        if (key.contains("error") || key.contains("rusak")) {
            return new ErrorGlitchGimmick();
        }
        if (key.contains("gempa") || key.contains("shake")) {
            return new ShakeGimmick(); 
        }

        // --- 2. CEK GIMMICK FILE ---
        File videoFile = new File("data/video/" + key + ".mp4");
        if (videoFile.exists()) return new VideoGimmick(videoFile);

        File imgFileJpg = new File("data/images/" + key + ".jpg");
        File imgFilePng = new File("data/images/" + key + ".png");
        File imgFile = imgFileJpg.exists() ? imgFileJpg : (imgFilePng.exists() ? imgFilePng : null);
        File audioFile = new File("data/audio/" + key + ".wav");

        if (imgFile != null || audioFile != null) {
            return new ImageAudioGimmick(keyword, imgFile, audioFile);
        }

        return new NullGimmick();
    }


    // Gimmick 1: Tulisan Miring (Italic)
    private static class ItalicTextGimmick implements Gimmick {
        @Override
        public void execute(DictionaryUI ui) {
            JTextArea textArea = ui.getTxtResultDesc();
            Font oldFont = textArea.getFont();
            // Ubah font jadi Miring (Italic)
            textArea.setFont(new Font(oldFont.getName(), Font.ITALIC, oldFont.getSize()));
        }
    }

    // Gimmick 2: Layar Blur (Menggunakan GlassPane)
    private static class BlurScreenGimmick implements Gimmick {
        @Override
        public void execute(DictionaryUI ui) {
            JPanel glass = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(255, 255, 255, 200)); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            glass.setOpaque(false);

            ui.setGlassPane(glass);
            ui.getGlassPane().setVisible(true);

            Timer timer = new Timer(3000, e -> ui.getGlassPane().setVisible(false));
            timer.setRepeats(false);
            timer.start();
        }
    }

    // Gimmick 3: Error / Glitch 
    private static class ErrorGlitchGimmick implements Gimmick {
        @Override
        public void execute(DictionaryUI ui) {
            ui.getContentPane().setBackground(Color.RED);
            ui.getTxtResultDesc().setText("SYSTEM FAILURE... SYSTEM FAILURE...\nJANGAN PANIK!");
            ui.getTxtResultDesc().setForeground(Color.WHITE);
        }
    }


    private static class VideoGimmick implements Gimmick {
        private final File videoFile;
        public VideoGimmick(File f) { this.videoFile = f; }
        @Override
        public void execute(DictionaryUI ui) { 
            new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(videoFile);
                } catch (IOException e) { e.printStackTrace(); }
            }).start();
        }
    }

    private static class ImageAudioGimmick implements Gimmick {
        private final String title;
        private final File imgFile;
        private final File audioFile;

        public ImageAudioGimmick(String t, File i, File a) { title = t; imgFile = i; audioFile = a; }

        @Override
        public void execute(DictionaryUI ui) {
            // Audio Logic
            if (audioFile != null && audioFile.exists()) {
                new Thread(() -> {
                    try {
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                    } catch (Exception e) {}
                }).start();
            }
            
            // Image Logic 
            if (imgFile != null && imgFile.exists()) {
                JWindow popup = new JWindow(ui); 
                try { popup.setBackground(new Color(0,0,0,0)); } catch (Exception e) {}
                
                JPanel content = new JPanel(new BorderLayout());
                content.setOpaque(false);
                
                ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(350, -1, Image.SCALE_SMOOTH);
                
                JLabel lblImg = new JLabel(new ImageIcon(img));
                JLabel lblCaption = new JLabel(title, SwingConstants.CENTER);
                lblCaption.setOpaque(true);
                lblCaption.setBackground(new Color(255,255,255,200));
                
                content.add(lblImg, BorderLayout.CENTER);
                content.add(lblCaption, BorderLayout.SOUTH);
                popup.add(content);
                popup.pack();
                popup.setLocationRelativeTo(null);
                popup.setVisible(true);
                popup.setAlwaysOnTop(true);
                
                new Timer(3000, e -> popup.dispose()).start();
            }
        }
    }

    private static class ShakeGimmick implements Gimmick {
        @Override
        public void execute(DictionaryUI ui) {
            Point originalLoc = ui.getLocation();
            
            Timer timer = new Timer(50, null);
            final int[] count = {0}; 
            
            timer.addActionListener(e -> {
                int xOffset = (int) (Math.random() * 20 - 10);
                int yOffset = (int) (Math.random() * 20 - 10);
                ui.setLocation(originalLoc.x + xOffset, originalLoc.y + yOffset);
                
                count[0]++;
                if (count[0] >= 20) {
                    ui.setLocation(originalLoc); 
                    timer.stop();
                }
            });
            timer.start();
        }
    }

    private static class NullGimmick implements Gimmick {
        @Override
        public void execute(DictionaryUI ui) { }
    }
}