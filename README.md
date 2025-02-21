# Library Management System

## About the Project
This is a simple Library Management System** built with Java, JDBC, and MySQL.  It helps users borrow and return books, and lets admins add or remove books.

Features
- Users can:
  - See available books
  - Borrow books (if they are available)
  - Return borrowed books
  - View their current loans
- Admins can:
  - Add books to the library
  - Remove books from the library
  - View all books and their loan status


How to Set Up
1. Clone the Project

2. Set Up the Database
Run this SQL in MySQL:
```sql
CREATE DATABASE library_db;
USE library_db;

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'member') NOT NULL DEFAULT 'member'
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category_id INT,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    loan_date DATE,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```
3. Configure Database Connection


4. Run the Program
- Run the Main.java file.

How to Use
Logging in as an Admin
```
Enter role: admin
Enter password: adminpass
```
Admin Menu:
```
1. View books
2. Borrow a book
3. Return a book
4. Add a book
5. Remove a book
6. Manage categories
7. Exit
```

 Logging in as a User
```
Enter role: member
Enter password: userpass
```
User Menu:
```
1. View books
2. Borrow a book
3. Return a book
4. Exit
```

Notes
- Admins can manage books and categories, while users can only borrow and return books.
- Uses MySQL as the database.
- The project is a simple console-based application.



