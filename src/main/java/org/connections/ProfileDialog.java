package org.connections;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class ProfileDialog extends JDialog {

    private JTextArea bioTextArea;
    private JLabel imageLabel;
    private byte[] newImageData; // To hold the bytes of a newly selected image

    public ProfileDialog(Frame owner) {
        super(owner, "My Profile", true);

        // --- UI Setup ---
        imageLabel = new JLabel("No Image Uploaded");
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton uploadButton = new JButton("Upload Image");

        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(uploadButton, BorderLayout.SOUTH);

        bioTextArea = new JTextArea(10, 30);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);

        JButton saveButton = new JButton("Save");
        JButton closeButton = new JButton("Close");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        // --- Layout ---
        setLayout(new BorderLayout(10, 10));
        add(imagePanel, BorderLayout.WEST);
        add(bioScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        uploadButton.addActionListener(e -> selectImageFile());
        saveButton.addActionListener(e -> saveProfile());
        closeButton.addActionListener(e -> dispose());

        // --- Load existing data from DB ---
        loadProfile();

        pack();
        setLocationRelativeTo(owner);
    }

    private void loadProfile() {
        String sql = "SELECT bio, image_data FROM profile WHERE id = 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                bioTextArea.setText(rs.getString("bio"));
                byte[] imageData = rs.getBytes("image_data");
                if (imageData != null && imageData.length > 0) {
                    displayImage(imageData);
                    this.newImageData = imageData; // Store existing image data
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load profile.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectImageFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                this.newImageData = Files.readAllBytes(file.toPath());
                displayImage(this.newImageData);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to read image file.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(byte[] imageData) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
            Image scaledImage = bufferedImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText(""); // Remove "No Image" text
        } catch (IOException e) {
            e.printStackTrace();
            imageLabel.setText("Failed to display image");
        }
    }

    private void saveProfile() {
        String bioText = bioTextArea.getText();
        String sql = "UPDATE profile SET bio = ?, image_data = ? WHERE id = 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bioText);
            pstmt.setBytes(2, newImageData);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile saved successfully!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save profile.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}