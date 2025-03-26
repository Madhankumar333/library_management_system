package libmansys;

import java.sql.*;
import java.util.Scanner;

public class library {
    static Scanner s = new Scanner(System.in);

    public void login(String url, String user, String pass1) {
        try {
            Connection con = DriverManager.getConnection(url, user, pass1);
            System.out.println("Connected to the database successfully!");

            System.out.println("ENTER YOUR EMAIL:");
            String email = s.next();
            System.out.println("ENTER YOUR PASSWORD:");
            String e_mpass = s.next();

            String query = "SELECT email, password FROM employee WHERE email = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, e_mpass);
            ResultSet z = pstmt.executeQuery();

            if (z.next()) {
                System.out.println("Login successful! Welcome, " + email);
                option(con); 
            } else {
                System.out.println("Invalid email or password.");
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void option(Connection con) {
        System.out.println("ENTER THE OPTION : ");
        System.out.println("1. View book");
        System.out.println("2. Add book");
        System.out.println("3. Issue book");
        System.out.println("4. Consumer");
        System.out.println("5. Log out");
        
        int option = s.nextInt();
        
        switch(option) {
            case 1:
                System.out.println("<--view book-->");
                ViewingBook viewer = new ViewingBook(con);
                viewer.search();
                break;
            case 2:
                //System.out.println("<--addbook-->");
                System.out.println("<--addbook-->");
                addbook(con); 
                break;
            case 3:
                System.out.println("<--issuebook-->");
                issuingbook issuer = new issuingbook(con); // Pass the connection
                issuer.issue();
                break;
            case 4:
                System.out.println("<--cousmer-->");
                consumer(con);
                break;
            case 5:
                System.out.println("<--log out-->");
                logout(con);
                break;
            default:
                System.out.println("INVALID OPTION");
        }
    }

    private void ViewingBook(Connection con) {
		// TODO Auto-generated method stub
		
	}

	public void viewbook(Connection con) {
       ViewingBook ss=new ViewingBook(con);
    }

    public void addbook(Connection con) {
    	
            System.out.println("--- ADD NEW BOOK ---");
            
            
            Scanner ss=new Scanner(System.in);
            System.out.println("Enter Book ID: ");
            
            int bookId = ss.nextInt();
            s.nextLine(); 
            
            System.out.println("Enter Book Name: ");
            String bookName = ss.nextLine();
            
            System.out.println("Enter Author: ");
            String author = ss.nextLine();
            
            System.out.println("Enter Genre: ");
            String genre = ss.nextLine();
            
            System.out.println("Enter Number of Copies: ");
            int numCopies = ss.nextInt();
            String query = "INSERT INTO Books1 (BookID, BookName, Author, Genre, NumberOfCopies, BorrowedCount) VALUES (?, ?, ?, ?, ?, 0)"; 
      
      
      try  {
    	  PreparedStatement pstmt = con.prepareStatement(query);
          pstmt.setInt(1, bookId);
          pstmt.setString(2, bookName);
          pstmt.setString(3, author);
          pstmt.setString(4, genre);
          pstmt.setInt(5, numCopies);
          int rowsAffected = pstmt.executeUpdate();
          
          if (rowsAffected >= 1) {
              System.out.println("Book added successfully");
          } else {
              System.out.println("Failed to add book.");
          }
  } catch (SQLException e) {
      System.out.println("Error adding book: " + e.getMessage());
  }
}

    public void issuebook(Connection con) {
       
        	 issuingbook sss=new  issuingbook(con);
    }
            
    public void consumer(Connection con) {
    	
        
    }

    public void logout(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
            System.out.println("Logged out successfully!");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}