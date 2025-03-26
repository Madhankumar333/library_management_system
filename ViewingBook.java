package libmansys;

import java.sql.*;
import java.util.Scanner;

public class ViewingBook {
    private Connection con;
    private Scanner scanner = new Scanner(System.in);

    public ViewingBook(Connection connection) {
        this.con = connection;
    }

    public void search() {
    	while(true) {
        try {
            System.out.println("\n--- BOOK SEARCH ---");
            System.out.println("Search by: ");
            System.out.println("1. Book Title");
            System.out.println("2. Author");
            System.out.println("3. Genre");
            System.out.println("4. EXIT");
            System.out.print("Enter your choice (1-4): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
           if(choice==4)
            System.out.print("Enter your search term: ");
            String searchTerm = scanner.nextLine();
            
            
            String query = "";
            switch(choice) {
                case 1:
                    query = "SELECT * FROM books WHERE BookName LIKE ?";
                    break;
                case 2:
                    query = "SELECT * FROM books WHERE Author LIKE ?";
                    break;
                case 3:
                    query = "SELECT * FROM books WHERE Genre LIKE ?";
                    break;
                case 4:
                    try {
                        if(con != null) con.close();
                        System.out.println("--THANK YOU--");
                    } catch (SQLException e) {
                        System.out.println("Error closing connection: " + e.getMessage());
                    }
                    return;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
        
            
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                
                ResultSet rs = pstmt.executeQuery();
                
                System.out.println("\nSearch Results:");
                System.out.printf("| %-5s | %-30s | %-20s | %-15s | \n", 
                                 "ID", "Book Name", "Author", "Genre");
                
                boolean found = false;
                while(rs.next()) {
                    found = true;
                    int bookId = rs.getInt("BookID");
                    String bookName = rs.getString("BookName");
                    String author = rs.getString("Author");
                    String genre = rs.getString("Genre");
                   // int available = rs.getInt("NumberOfCopies") - rs.getInt("BorrowedCount");
                    
                    System.out.printf("| %-5d | %-30s | %-20s | %-15s |2\n", 
                                    bookId, bookName, author, genre);

                }
                
                if(!found) {
                    System.out.println("No books found matching your search criteria.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    }
}

        