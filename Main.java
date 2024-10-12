import java.util.Scanner;

// Book class to represent library books
class Book {
    int id;
    String title;
    String author;
    boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "', available=" + isAvailable + "}";
    }
}

// Node class for Binary Search Tree
class BSTNode {
    Book book;
    BSTNode left, right;

    public BSTNode(Book book) {
        this.book = book;
        left = right = null;
    }
}

// Queue implementation for book reservations
class ReservationQueue {
    class QNode {
        int bookId;
        String memberName;
        QNode next;

        public QNode(int bookId, String memberName) {
            this.bookId = bookId;
            this.memberName = memberName;
            this.next = null;
        }
    }

    QNode front, rear;

    public ReservationQueue() {
        front = rear = null;
    }

    public void enqueue(int bookId, String memberName) {
        QNode newNode = new QNode(bookId, memberName);
        
        if (rear == null) {
            front = rear = newNode;
            return;
        }
        
        rear.next = newNode;
        rear = newNode;
    }

    public QNode dequeue() {
        if (front == null) return null;
        
        QNode temp = front;
        front = front.next;
        
        if (front == null) rear = null;
        
        return temp;
    }
}

// Library Management System class
class LibraryManagementSystem {
    private BSTNode root;
    private ReservationQueue reservations;

    public LibraryManagementSystem() {
        root = null;
        reservations = new ReservationQueue();
    }

    // Insert a book into BST
    public void addBook(Book book) {
        root = insertRec(root, book);
    }

    private BSTNode insertRec(BSTNode root, Book book) {
        if (root == null) {
            root = new BSTNode(book);
            return root;
        }

        if (book.id < root.book.id)
            root.left = insertRec(root.left, book);
        else if (book.id > root.book.id)
            root.right = insertRec(root.right, book);

        return root;
    }

    // Search for a book in BST
    public Book searchBook(int id) {
        BSTNode node = searchRec(root, id);
        return node != null ? node.book : null;
    }

    private BSTNode searchRec(BSTNode root, int id) {
        if (root == null || root.book.id == id)
            return root;

        if (root.book.id > id)
            return searchRec(root.left, id);

        return searchRec(root.right, id);
    }

    // Reserve a book
    public void reserveBook(int bookId, String memberName) {
        Book book = searchBook(bookId);
        if (book != null && !book.isAvailable) {
            reservations.enqueue(bookId, memberName);
            System.out.println("Book reserved for " + memberName);
        } else if (book != null && book.isAvailable) {
            System.out.println("Book is available, no need to reserve");
        } else {
            System.out.println("Book not found!");
        }
    }

    // Process next reservation
    public void processNextReservation() {
        ReservationQueue.QNode reservation = reservations.dequeue();
        if (reservation != null) {
            Book book = searchBook(reservation.bookId);
            if (book != null && !book.isAvailable) {
                book.isAvailable = true;
                System.out.println("Reservation processed for " + reservation.memberName + " - Book: " + book.title);
            }
        } else {
            System.out.println("No reservations in queue");
        }
    }

    // Borrow a book
    public void borrowBook(int id) {
        Book book = searchBook(id);
        if (book != null && book.isAvailable) {
            book.isAvailable = false;
            System.out.println("Book borrowed successfully: " + book.title);
        } else if (book != null) {
            System.out.println("Book is not available: " + book.title);
        } else {
            System.out.println("Book not found");
        }
    }

    // Return a book
    public void returnBook(int id) {
        Book book = searchBook(id);
        if (book != null && !book.isAvailable) {
            book.isAvailable = true;
            System.out.println("Book returned successfully: " + book.title);
        } else if (book != null) {
            System.out.println("Book was already in library: " + book.title);
        } else {
            System.out.println("Book not found");
        }
    }

    //Dislay all book

    public void displayAllBooks(){
        System.out.println("All books in the library:");
        inorderTraversal(root);
    }

    private void inorderTraversal(BSTNode node){
        if (node != null){
            inorderTraversal(node.left);
            System.out.println(node.book);
            inorderTraversal(node.right);
        }
    }

    //check if the library is empty
    public boolean isEmpty(){
        return root == null;
    }
}


// Main class to demonstrate the Library Management System
public class Main {
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner scan = new Scanner(System.in);
        int bookid = 1;

        while(true){

            System.out.println("\nWelcome to the Library!\nHow may I help you?\n1. Add book\n2. Borrow book\n3. Reserve book\n4. Return book\n5. Show all Books\n6. Book reservation process\n7. Exit");
            if (scan.hasNextInt()){
                int start = scan.nextInt();
                scan.nextLine();
    
                switch (start) {
                    case 1:
                        while (true){
                            System.out.print("Please enter the book name: ");
                            String bookName = scan.nextLine();
                            System.out.print("Please enter the book author: ");
                            String bookAuthor = scan.nextLine();
    
                            library.addBook(new Book(bookid, bookName, bookAuthor));
    
                            System.out.println("Successfully added book '" + bookName + "' by " + bookAuthor);
                            System.out.println("Would you like to add more books?\n1. Yes\n2. No");
                            int morebook = scan.nextInt();
                            scan.nextLine();
                            
                            while(morebook != 1 && morebook != 2){
                                System.out.print("Invalid, please enter a valid number: ");
                                morebook = scan.nextInt();
                                scan.nextLine();
                            }
    
                            switch (morebook) {
                                case 1:
                                    bookid++;
                                    continue;
                                case 2:
                                    break;
                                default:
                                    
                            }
                            break;
                        }
                        break;
    
                    case 2:
                        library.displayAllBooks();
                        System.out.print("Please enter the book ID you'd like to borrow: ");
                        int searchid = scan.nextInt();
                        scan.nextLine();
                        library.borrowBook(searchid);
                        break;
                    
                    case 3:
                        if (library.isEmpty()){
                            System.out.println("The library is empty. No books available for reservation.");
                        } else{
                            library.displayAllBooks();
                            System.out.print("Please enter the book ID you'd like to reserve: ");
                            int reserveId = scan.nextInt();
                            scan.nextLine();
    
                            System.out.print("Please enter your name: ");
                            String memberName = scan.nextLine();
    
                            library.reserveBook(reserveId, memberName);
                        }
                        break;
    
                    case 4:
                        if (library.isEmpty()){
                            System.out.println("The library is empty. No books available for return.");
                        } else{
                            library.displayAllBooks();
                            System.out.print("Please enter the ID of the book you'd like to return: ");
                            int returnId = scan.nextInt();
                            scan.nextLine();
                            library.returnBook(returnId);
                        }
                        break;
                        
                    case 5:
                        if (library.isEmpty()){
                            System.out.println("The library is empty. Would you like to add a book?\n1. Yes\n2. No");
                            int addBookChoice = scan.nextInt();
                            scan.nextLine();
                            if (addBookChoice == 1){
                                System.out.print("Please enter the book name: ");
                                String bookName = scan.nextLine();
                                System.out.print("Please enter the book author: ");
                                String bookAuthor = scan.nextLine();
                                library.addBook(new Book(bookid, bookName, bookAuthor));
                                System.out.println("Successfully added book '" + bookName + "' by " + bookAuthor);
                                bookid++;
                            } else {
                                System.out.println("I understand.");
                                break;
                            }
                        } else {
                            library.displayAllBooks();
                        }
                        break;
                    
                    case 6:
                        library.processNextReservation();
                        break;

                    case 7:
                        System.out.println("Thank you for using the Library");
                        scan.close();
                        return;
                    default:
                        System.out.print("invalid, please enter a valid number: ");
                        scan.nextLine();
                }
            }  
        }
    }
}