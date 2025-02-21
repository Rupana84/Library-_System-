import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(String title, String author, int categoryId) {
        String query = "INSERT INTO books (title, author, category_id, available) VALUES (?, ?, ?, TRUE)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, categoryId);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }
        // to remove book
    public boolean removeBook(int bookId) {
        String checkQuery = "SELECT * FROM books WHERE id = ?";
        String deleteQuery = "DELETE FROM books WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Book not found.");
                return false;
            }

            deleteStmt.setInt(1, bookId);
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book removed successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error removing book: " + e.getMessage());
        }
        return false;
    }
    // check all the books
    public List<String> getAllBooks() {
        List<String> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String availability = rs.getBoolean("available") ? "Available" : "Not Available";
                books.add(rs.getInt("id") + ". " +
                        rs.getString("title") +
                        " by " + rs.getString("author") +
                        " (" + availability + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }
    // to get Available books
    public List<String> getAvailableBooks() {
        List<String> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE available = TRUE";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(rs.getInt("id") + ". " + rs.getString("title") + " by " + rs.getString("author") + " (Available)");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available books: " + e.getMessage());
        }
        return books;
    }
}