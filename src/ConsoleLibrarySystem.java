import java.util.List;
import java.util.Scanner;

public class ConsoleLibrarySystem {
    private String role;
    private static Scanner scanner = new Scanner(System.in);
    private static BookDAO bookDAO = new BookDAO();
    private static LoanDAO loanDAO = new LoanDAO();

    public ConsoleLibrarySystem(String role) {
        this.role = role;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. List all books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");

            if ("admin".equals(role)) {
                System.out.println("4. Add a book");
                System.out.println("5. Remove a book");
            }

            System.out.println("6. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> listBooks();
                case 2 -> borrowBook();
                case 3 -> returnBook();
                case 4 -> {
                    if ("admin".equals(role)) addBook();
                    else System.out.println("Unauthorized access!");
                }
                case 5 -> {
                    if ("admin".equals(role)) removeBook();
                    else System.out.println("Unauthorized access!");
                }
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void listBooks() {
        List<String> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private static void borrowBook() {
        System.out.print("Enter your user ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter book ID to borrow: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        boolean success = loanDAO.borrowBook(userId, bookId);
        if (success) {
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Error: This book is not available or another issue occurred.");
        }
    }

    private void returnBook() {
        System.out.print("Enter your user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.println("\nYour borrowed books:");
        loanDAO.getUserLoans(userId);

        System.out.print("\nEnter the book ID to return: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        boolean success = loanDAO.returnBook(userId, bookId);
        if (success) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Error: You have not borrowed this book.");
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        bookDAO.addBook(title, author, categoryId);
    }

    private static void removeBook() {
        System.out.print("Enter book ID to remove: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        boolean success = bookDAO.removeBook(bookId);
        if (success) {
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Error: Book not found.");
        }
    }
}