import java.util.ArrayList;

public class Shelf {
    private ArrayList<Book> books;
    private ArrayList<Book> ownedBooks; //馆藏书籍

    public Shelf() {
        this.books = new ArrayList<>();
        this.ownedBooks = new ArrayList<>();
    }

    public void addOwnedBook(Book book) {
        ownedBooks.add(book);
    }

    public void removeOwnedBook(Book book) {
        ownedBooks.remove(book);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addBooks(ArrayList<Book> newBooks) {
        books.addAll(newBooks);
    }

    public Book query(String type, String id) {
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public Book queryOut(String type, String id) {
        for (Book book : books) {
            if (book.getType().equals(type) && book.getId().equals(id) && book.isAllowOut()) {
                return book;
            }
        }
        return null;
    }

    public boolean ownedEver(String type, String id) {
        for (Book book : ownedBooks) {
            if (book.getType().equals(type) && book.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
