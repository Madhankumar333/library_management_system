package libmansys;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class home {
    static Scanner s = new Scanner(System.in);
    
    public static void main(String args[]) throws SQLException {
        System.out.println("<--WELCOME-->");
        System.out.println("ENTER {1} FOR EMPLOYEE: ");
        System.out.println("ENTER {2} FOR STUDENT: ");
        int op = s.nextInt();
        
        if(op == 1) {
            String url = "jdbc:mysql://localhost:3306/library";
            System.out.print("ENTER YOUR DATABASE USER NAME:");
            String user = s.next();
            System.out.print("ENTER YOUR DATABASE USER PASSWORD:");
            String pass1 = s.next();
            
            library lib = new library();
            lib.login(url, user, pass1);
        }
        else if(op == 2) {
        	String url = "jdbc:mysql://localhost:3306/LIBRARY";
            Student student = new Student(url);
            student.studentMenu();
        }
        else {
            System.out.println("ERROR:-ENTER THE VALID OPTION");
        }
    }
}
