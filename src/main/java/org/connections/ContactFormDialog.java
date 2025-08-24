package org.connections;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A dialog window for adding a NEW contact or editing an EXISTING one.
 */
public class ContactFormDialog extends JDialog {

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;

    // This will hold the ID of the contact we are editing.
    // It will be 0 for a new contact.
    private int contactIdToUpdate = 0;

    /**
     * Constructor for ADDING a new contact.
     */
    public ContactFormDialog(Frame owner) {
        super(owner, "Add New Contact", true);
        initComponents();
    }

    /**
     * NEW: Constructor for EDITING an existing contact.
     */
    public ContactFormDialog(Frame owner, int contactId, String name, String phone, String email) {
        super(owner, "Edit Contact", true);
        initComponents();

        // Store the ID and pre-fill the form fields with existing data
        this.contactIdToUpdate = contactId;
        nameField.setText(name);
        phoneField.setText(phone);
        emailField.setText(email);
    }

    /**
     * Helper method to create and lay out the UI components.
     */
    private void initComponents() {
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Handles the logic for saving. Performs INSERT for new contacts
     * and UPDATE for existing ones.
     */
    private void saveContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Differentiate between ADD and EDIT logic
        if (contactIdToUpdate == 0) {
            // This is a NEW contact (INSERT)
            executeInsert(name, phone, email);
        } else {
            // This is an EXISTING contact (UPDATE)
            executeUpdate(name, phone, email);
        }
    }

    private void executeInsert(String name, String phone, String email) {
        String sql = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Contact saved successfully!");
            ((ContactListFrame) getOwner()).loadContacts();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving new contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeUpdate(String name, String phone, String email) {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setInt(4, this.contactIdToUpdate); // The ID of the row to update
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Contact updated successfully!");
            ((ContactListFrame) getOwner()).loadContacts();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}