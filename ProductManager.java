import java.sql.*;
import java.util.Scanner;

public class ProductManager {
    private static final String URL = "jdbc:mysql://localhost:3306/ShopDB";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change based on your MySQL setup
    private static Connection conn;

    public static void main(String[] args) {
        try {
            // Step 1: Load the JDBC driver and connect to database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Enable transaction handling

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n---- Product Management System ----");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addProduct(scanner);
                        break;
                    case 2:
                        viewProducts();
                        break;
                    case 3:
                        updateProduct(scanner);
                        break;
                    case 4:
                        deleteProduct(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        conn.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. CREATE Operation - Add a new product
    private static void addProduct(Scanner scanner) {
        try {
            System.out.print("Enter Product Name: ");
            scanner.nextLine();
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Product added successfully.");
            }

        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback on failure
                System.out.println("Transaction failed! Rolling back changes.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // 2. READ Operation - View all products
    private static void viewProducts() {
        try {
            String query = "SELECT * FROM Product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nProduct List:");
            System.out.println("-----------------------------------");
            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("ProductName");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");
                System.out.println(id + " | " + name + " | ₹" + price + " | " + quantity + " units");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. UPDATE Operation - Modify product details
    private static void updateProduct(Scanner scanner) {
        try {
            System.out.print("Enter Product ID to update: ");
            int productID = scanner.nextInt();
            System.out.print("Enter New Price: ");
            double newPrice = scanner.nextDouble();
            System.out.print("Enter New Quantity: ");
            int newQuantity = scanner.nextInt();

            String query = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, newQuantity);
            pstmt.setInt(3, productID);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product ID not found.");
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Transaction failed! Rolling back changes.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // 4. DELETE Operation - Remove a product
    private static void deleteProduct(Scanner scanner) {
        try {
            System.out.print("Enter Product ID to delete: ");
            int productID = scanner.nextInt();

            String query = "DELETE FROM Product WHERE ProductID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, productID);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product ID not found.");
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Transaction failed! Rolling back changes.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
