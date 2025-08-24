package org.connections;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ContactListFrame extends JFrame {

    private JTable contactTable;
    private DefaultTableModel tableModel;

    public ContactListFrame() {
        setTitle("Contact Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initMenuBar();
        String[] columnNames = {"ID", "Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make the table cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contactTable = new JTable(tableModel);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Contact");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(contactTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENERS ---

        addButton.addActionListener(e -> {
            ContactFormDialog addDialog = new ContactFormDialog(this);
            addDialog.setVisible(true);
        });

        editButton.addActionListener(e -> editSelectedContact());

        deleteButton.addActionListener(e -> deleteSelectedContact());

        loadContacts();
    }

    private void editSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a contact to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get data from the selected row in the table model
        int contactId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String phone = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);

        // Open the dialog in "edit mode"
        ContactFormDialog editDialog = new ContactFormDialog(this, contactId, name, phone, email);
        editDialog.setVisible(true);
    }
    /**
     * Creates and sets up the menu bar for the frame.
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("About");
        // Changed the item text to be more descriptive
        JMenuItem profileItem = new JMenuItem("My Profile");

        profileItem.addActionListener(e -> {
            // Create and show the new Profile dialog
            ProfileDialog profileDialog = new ProfileDialog(this);
            profileDialog.setVisible(true);
        });

        helpMenu.add(profileItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar); // Set the menu bar for this frame
    }

    private void deleteSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int contactId = (int) tableModel.getValueAt(selectedRow, 0);
        String contactName = (String) tableModel.getValueAt(selectedRow, 1);

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + contactName + "'?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM contacts WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, contactId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Contact deleted successfully.");
                loadContacts(); // Refresh the table

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadContacts() {
        String sql = "SELECT id, name, phone, email FROM contacts ORDER BY name";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("email"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}