package libmansys;

import java.sql.*;
import java.util.Scanner;

public class issuingbook {
    private Connection con;
    private Scanner input;
    
    // Constructor should have the same name as the class
    public issuingbook(Connection connection) {
        this.con = connection;
        this.input = new Scanner(System.in);
    }
    
    public void issue() {
    		while(true) {
        try {
            System.out.println("\n--- ISSUE BOOK ---");
            System.out.println("Available Books:");
            
            // Show available books
            String availableQuery = "SELECT * FROM Books WHERE Available > 0";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(availableQuery);
            
            System.out.printf("| %-5s | %-30s | %-20s | %-5s |\n", 
                           "ID", "Book Name", "Author", "Avail");
            
            while(rs.next()) {
                System.out.printf("| %-5d | %-30s | %-20s | %-5d |\n", 
                                rs.getInt("BookID"), 
                                rs.getString("BookName"),
                                rs.getString("Author"),
                                rs.getInt("Available"));
            }
            System.out.print("\nEnter Customer ID (0 to exit): ");  // Added exit option
            int customerId = input.nextInt();
            if (customerId == 0) break;  // Exit condition
            
            // Get user input
            System.out.print("\nEnter Customer ID: ");
            int customerId1 = input.nextInt();
            
            System.out.print("Enter Book ID to issue: ");
            int bookId = input.nextInt();
            
            // Transaction handling
            con.setAutoCommit(false);
            
            try {
                // Check availability with lock
                String checkSql = "SELECT Available FROM Books WHERE BookID = ? AND Available > 0 FOR UPDATE";
                PreparedStatement checkStmt = con.prepareStatement(checkSql);
                checkStmt.setInt(1, bookId);
                ResultSet checkRs = checkStmt.executeQuery();
                
                if (!checkRs.next()) {
                    System.out.println("Book not available for issuing!");
                    con.rollback();
                    return;
                }
                
                String borrowSql = "INSERT INTO borrow (customerId, BookID) VALUES (?, ?)";
                PreparedStatement borrowStmt = con.prepareStatement(borrowSql);
                borrowStmt.setInt(1, customerId1);
                borrowStmt.setInt(2, bookId);
                borrowStmt.executeUpdate();
                
                String updateSql = "UPDATE Books SET Available = Available - 1, " +
                                 "BorrowedCount = BorrowedCount + 1 WHERE BookID = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setInt(1, bookId);
                int updated = updateStmt.executeUpdate();
                
                if (updated == 1) {
                    con.commit();
                    System.out.println("Book issued successfully!");
                } else {
                    con.rollback();
                    System.out.println("Failed to issue book!");
                }
                
            } catch (SQLException e) {
                con.rollback();
                if (e.getMessage().contains("Duplicate entry")) {
                    System.out.println("Error: This customer already has this book!");
                } else {
                    System.out.println("Error issuing book: " + e.getMessage());
                }
            } finally {
                con.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    }
}

	