import java.sql.*;

public class LoanDAO {
    // Borrow a book
    public boolean borrowBook(int userId, int bookId) {
        String checkAvailability = "SELECT available FROM books WHERE id = ?";
        String borrowQuery = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, CURRENT_DATE)";
        String updateBook = "UPDATE books SET available = FALSE WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkAvailability);
             PreparedStatement borrowStmt = conn.prepareStatement(borrowQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateBook)) {

            // Check if book is available
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && !rs.getBoolean("available")) {
                System.out.println("This book is already borrowed.");
                return false;
            }

            // Borrow the book
            borrowStmt.setInt(1, userId);
            borrowStmt.setInt(2, bookId);
            borrowStmt.executeUpdate();

            // Mark book as unavailable
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.err.println("Error borrowing book: " + e.getMessage());
            return false;
        }
    }

    // Return a book
    public boolean returnBook(int userId, int bookId) {
        String query = "DELETE FROM loans WHERE user_id = ? AND book_id = ? LIMIT 1";
        String updateBookQuery = "UPDATE books SET available = TRUE WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery)) {

                // Delete loan record
                stmt.setInt(1, userId);
                stmt.setInt(2, bookId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Mark book as available again
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                    conn.commit(); // Commit transaction
                    System.out.println("Book returned successfully!");
                    return true;
                } else {
                    conn.rollback(); // Rollback if book was not borrowed
                    System.out.println("Error: No loan record found for this book and user.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            return false;
        }
    }

    // Get a list of books currently borrowed by a user
    public void getUserLoans(int userId) {
        String query = "SELECT b.id, b.title, b.author FROM loans l +" +
                "JOIN books b ON l.book_id = b.id +" +
                "WHERE l.user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nYour Borrowed Books:");
            boolean hasBooks = false;

            while (rs.next()) {
                hasBooks = true;
                System.out.println(rs.getInt("id") +
                        ". " + rs.getString("title") +
                        " by " + rs.getString("author"));
            }

            if (!hasBooks) {
                System.out.println("You have no borrowed books.");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching borrowed books: " + e.getMessage());
        }
    }
}