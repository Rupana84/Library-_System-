import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        System.out.print("Enter role (admin/member): ");
        String role = scanner.next();  // User enters role
        System.out.print("Enter password: ");
        String password = scanner.next();

        String authenticatedRole = userDAO.authenticateUser(role, password);

        if (authenticatedRole == null) {
            System.out.println("Invalid role or password.");
            return;
        }

        // For passes the 'role'
        ConsoleLibrarySystem librarySystem = new ConsoleLibrarySystem(authenticatedRole);
        librarySystem.showMenu();
    }
}