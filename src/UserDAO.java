import java.sql.*;

public class UserDAO {
    public String authenticateUser(String role, String password) {
        String query = "SELECT role FROM users WHERE role = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);  // Use role instead of email
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Returns 'admin' or 'member'
            } else {
                System.out.println("Invalid role or password. Please try again.");
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }
}