package libmansys;

import java.sql.*;
import java.util.Scanner;

public class Student {
    private Connection con;
    private Scanner scanner;
    
    public Student(String url) throws SQLException {
        this.con = DriverManager.getConnection(url, "student_user", "student_password");
        this.scanner = new Scanner(System.in);
    }
    
    public void studentMenu() {
        while(true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. View All Books");
            System.out.println("2. Search Books");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch(choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    searchBooks();
                    break;
                case 3:
                    try {
                        if(con != null) con.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing connection: " + e.getMessage());
                    }
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private void viewAllBooks() {
        try {
            String query = "SELECT BookID, BookName, Author, Genre, Available FROM Books";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            System.out.println("\nALL BOOKS:");
            System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-5s |\n", 
                           "ID", "Book Name", "Author", "Genre", "Avail");
            
            while(rs.next()) {
                System.out.printf("| %-5d | %-30s | %-20s | %-10s | %-5d |\n", 
                                rs.getInt("BookID"), 
                                rs.getString("BookName"),
                                rs.getString("Author"),
                                rs.getString("Genre"),
                                rs.getInt("Available"));
            }
            
        } catch (SQLException e) {
            System.out.println("Error viewing books: " + e.getMessage());
        }
    }
    
    private void searchBooks() {
        try {
            System.out.println("\nSearch by:");
            System.out.println("1. Title");
            System.out.println("2. Author");
            System.out.println("3. Genre");
            System.out.print("Enter your choice: ");
            int searchType = scanner.nextInt();
            scanner.nextLine(); 
            
            System.out.print("Enter search term: ");
            String searchTerm = scanner.nextLine();
            
            String query = "SELECT BookID, BookName, Author, Genre, Available FROM Books WHERE ";
            switch(searchType) {
                case 1:
                    query += "BookName LIKE ?";
                    break;
                case 2:
                    query += "Author LIKE ?";
                    break;
                case 3:
                    query += "Genre LIKE ?";
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\nSEARCH RESULTS:");
            System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-5s |\n", 
                           "ID", "Book Name", "Author", "Genre", "Avail");
            
            boolean found = false;
            while(rs.next()) {
                found = true;
                System.out.printf("| %-5d | %-30s | %-20s | %-10s | %-5d |\n", 
                                rs.getInt("BookID"), 
                                rs.getString("BookName"),
                                rs.getString("Author"),
                                rs.getString("Genre"),
                                rs.getInt("Available"));
            }
            
            if(!found) {
                System.out.println("No books found matching your search.");
            }


        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }
}