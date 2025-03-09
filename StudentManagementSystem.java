import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USER = "root";  // Change this to your MySQL username
    private static final String PASSWORD = "";  // Change this to your MySQL password
    private static Connection conn;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);  // Enable transaction handling
            System.out.println(" Connected to MySQL Database!");

            while (true) {
                System.out.println("\n---- Student Management System ----");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        viewStudents();
                        break;
                    case 3:
                        updateStudent(scanner);
                        break;
                    case 4:
                        deleteStudent(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CREATE - Add a new student
    private static void addStudent(Scanner scanner) {
        try {
            scanner.nextLine();  // Consume newline
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter Marks: ");
            float marks = scanner.nextFloat();

            String query = "INSERT INTO Student (Name, Department, Marks) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setString(2, department);
                pstmt.setFloat(3, marks);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println(" Student added successfully!");
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    // READ - Display all students
    private static void viewStudents() {
        String query = "SELECT * FROM Student";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n---- Student Records ----");
            while (rs.next()) {
                System.out.println("StudentID: " + rs.getInt("StudentID") + 
                                   ", Name: " + rs.getString("Name") +
                                   ", Department: " + rs.getString("Department") +
                                   ", Marks: " + rs.getFloat("Marks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE - Modify student details
    private static void updateStudent(Scanner scanner) {
        try {
            System.out.print("Enter Student ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter New Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter New Department: ");
            String newDept = scanner.nextLine();
            System.out.print("Enter New Marks: ");
            float newMarks = scanner.nextFloat();

            String query = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, newName);
                pstmt.setString(2, newDept);
                pstmt.setFloat(3, newMarks);
                pstmt.setInt(4, id);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println(" Student updated successfully!");
                } else {
                    System.out.println(" Student not found!");
                }
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    // DELETE - Remove student by ID
    private static void deleteStudent(Scanner scanner) {
        try {
            System.out.print("Enter Student ID to delete: ");
            int id = scanner.nextInt();

            String query = "DELETE FROM Student WHERE StudentID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println(" Student deleted successfully!");
                } else {
                    System.out.println(" Student not found!");
                }
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
}
