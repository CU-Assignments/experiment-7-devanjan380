import java.sql.*;

public class EmployeeDataFetcher {
    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/CompanyDB"; // Change "CompanyDB" to your DB name
        String user = "root"; // Change to your MySQL username
        String password = ""; // Change to your MySQL password

        // SQL Query
        String query = "SELECT EmpID, Name, Salary FROM Employee";

        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish connection
            Connection conn = DriverManager.getConnection(url, user, password);

            // Step 3: Create Statement object
            Statement stmt = conn.createStatement();

            // Step 4: Execute Query
            ResultSet rs = stmt.executeQuery(query);

            // Step 5: Process the ResultSet
            System.out.println("Employee Records:");
            System.out.println("----------------------------");
            while (rs.next()) {
                int empID = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.println(empID + " | " + name + " | " + salary);
            }

            // Step 6: Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
        }
    }
}
