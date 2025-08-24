# Simple Contact Manager

## 1. Overview

Simple Contact Manager is a desktop application developed in Java Swing. It provides users with a clean and simple interface to manage their personal contacts. All contact information is securely stored in a PostgreSQL database, and the application includes a personalized "About Me" section that is also saved to the database.

## 2. Features

-   **Full Contact Management (CRUD)**:
    -   **Create**: Add new contacts with a name, phone number, and email address.
    -   **Read**: View all saved contacts in an organized table on the main screen.
    -   **Update**: Edit the details of any existing contact.
    -   **Delete**: Remove contacts with a confirmation dialog to prevent errors.
-   **Dynamic "About Me" Profile**:
    -   Unlike a static about page, this application features a dynamic profile page.
    -   Users can write their own biography and upload a profile picture.
    -   The profile information is saved to the database and can be edited at any time.
    -   The page includes a read-only "View Mode" and a functional "Edit Mode".
-   **Automated Database Setup**: On first launch, the application automatically creates the necessary `contacts` and `profile` tables in your PostgreSQL database, making setup effortless.

## 3. Technology Stack

-   **Language**: Java
-   **GUI Framework**: Java Swing
-   **Database**: PostgreSQL
-   **Build Tool**: Apache Maven (Recommended)
-   **Dependencies**: PostgreSQL JDBC Driver

## 4. Setup and Installation

Follow these steps to get the application running on your local machine.

### Prerequisites

-   JDK (Java Development Kit) v11 or newer.
-   Apache Maven.
-   A running PostgreSQL server instance.

### Step-by-Step Guide

1.  **Clone/Download the Project**: Get the source code onto your machine.

2.  **Create the Database**: In your PostgreSQL client, run the following command to create the database:
    ```sql
    CREATE DATABASE contact_manager_db;
    ```

3.  **Configure Database Connection**:
    -   Open the `DatabaseManager.java` file.
    -   Update the `DB_USER` and `DB_PASSWORD` constants with your PostgreSQL credentials.
    ```java
    private static final String DB_USER = "your_postgres_username";
    private static final String DB_PASSWORD = "your_postgres_password";
    ```

4.  **Build and Run the Application**:
    -   **From an IDE (Recommended)**:
        -   Open the project as a Maven project in your favorite IDE.
        -   The IDE will handle downloading the required dependencies.
        -   Locate and run the `main` method in the `Main.java` file.
    -   **From the Command Line**:
        -   Navigate to the project's root directory.
        -   Run the command: `mvn clean compile exec:java -Dexec.mainClass="your.package.name.Main"` (replace with the actual package name).

## 5. Code Structure

-   `Main.java`: The application's entry point, responsible for launching the main window.
-   `DatabaseManager.java`: Manages the database connection and automatic table creation.
-   `ContactListFrame.java`: The main application window that displays the list of all contacts.
-   `ContactFormDialog.java`: The dialog form used for both adding new contacts and editing existing ones.
-   `ProfileDialog.java`: The dynamic "About Me" page with view/edit modes for the user profile.
