# Connections - A Simple Contact Manager

`Connections` is a lightweight, user-friendly desktop application built with Java Swing for managing your personal and professional contacts. It provides a clean interface to view, add, edit, and delete contact information, storing all data locally in an SQLite database.



## Features

*   **View Contacts:** Displays all contacts in a clear, sortable table.
*   **Add Contact:** Easily add new contacts with their name, phone number, and email address.
*   **Edit Contact:** Select any contact from the list to update their information.
*   **Delete Contact:** Remove contacts you no longer need with a confirmation step to prevent accidental deletion.
*   **Persistent Storage:** All contact data is saved in a local SQLite database file (`contacts.db`), which is created automatically on the first run.
*   **Simple UI:** Built with standard Java Swing components for a familiar and responsive user experience.

## Prerequisites

Before you begin, ensure you have the following installed on your system:
*   Java Development Kit (JDK) (Version 8 or later)
*   Apache Maven (to manage dependencies and build the project)

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### 1. Clone the Repository

Open your terminal or command prompt and run the following command:

```bash
git clone https://github.com/your-username/Connections.git
cd Connections
```
*(Replace `your-username` with the actual repository location)*

### 2. Build the Project

The project uses Maven to handle dependencies (like the SQLite JDBC driver) and to compile the source code. Run the following command from the project's root directory:

```bash
mvn clean install
```

This will download the necessary libraries and create a runnable JAR file in the `target/` directory.

### 3. Run the Application

You can run the application in two ways:

**a) Using Maven:**

Execute the following command from the project's root directory. This is the recommended way for development.

```bash
mvn exec:java -Dexec.mainClass="org.connections.ContactListFrame"
```

**b) Running the JAR file:**

After building the project with `mvn clean install`, you can run the generated JAR file:

```bash
java -jar target/Connections-1.0-SNAPSHOT.jar
```
*(Note: The exact JAR filename may vary based on your `pom.xml` configuration.)*

## Database

The application uses an SQLite database named `contacts.db`.

*   **Automatic Creation:** You do not need to create this file yourself. The application will automatically generate it in the project's root directory on its first run if it doesn't already exist.
*   **Schema:** The database contains a single `contacts` table with the following columns: `id` (Primary Key), `name`, `phone`, and `email`.

## Project Structure

```
Connections/
├── pom.xml                 # Maven project configuration and dependencies
├── contacts.db             # SQLite database file (created on first run)
└── src/
    └── main/
        └── java/
            └── org/
                └── connections/
                    ├── ContactListFrame.java   # The main application window and entry point
                    ├── ContactFormDialog.java  # Dialog for adding/editing contacts
                    ├── ProfileDialog.java      # "About Me" profile dialog
                    └── DatabaseManager.java    # Handles database connection and setup
```