package org.connections;

import javax.swing.SwingUtilities;

/**
 * The main entry point for the Contact Manager application.
 */
public class Main {

    public static void main(String[] args) {
        // Run the database initialization first
        System.out.println("Starting application...");
        DatabaseManager.initializeDatabase();

        // All Swing UI components should be created and updated on the
        // Event Dispatch Thread (EDT). We use invokeLater for this.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create an instance of our main window
                ContactListFrame mainFrame = new ContactListFrame();
                // Make the window visible
                mainFrame.setVisible(true);
            }
        });
    }
}