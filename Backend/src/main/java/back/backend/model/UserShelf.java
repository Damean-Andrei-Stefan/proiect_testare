package back.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.UUID;

// Marchează această clasă ca o entitate JPA, care se mapează la o tabelă în baza de date
@Entity
public class UserShelf {

    // Marchează acest câmp ca fiind cheia primară a entității
    @Id
    private String id; // ID unic al raftului (generat automat)

    private String shelfName; // Numele raftului
    private int capacity;     // Capacitatea raftului (numărul maxim de cărți)


    public List<Book> getBooks() {
        return books;
    }

    @OneToMany // Relație "unu-la-mulți" între UserShelf și Book
    private List<Book> books; // Lista de cărți asociate acestui raft

    // Constructor implicit necesar pentru JPA
    public UserShelf() {
        this.id = generateUniqueId(); // Generează un ID unic pentru raft
    }


    public UserShelf(String shelfName, int capacity) {
        this.id = generateUniqueId();
        this.shelfName = shelfName;
        this.capacity = capacity;
    }

    // Generate unique ID using UUID
    private static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    // Id field doesn't need a setter, as it's generated
    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    // Metodă pentru afișarea detaliilor despre cărțile de pe raft
    public void displayBooks() {
        System.out.println("Books on Shelf " + shelfName + ":");
        if (books != null) { // Verifică dacă lista de cărți nu este null
            for (Book book : books) {
                // Afișează titlul, autorul și numărul de pagini pentru fiecare carte
                System.out.println("Title: " + book.getBookTitle() + ", Author: " + book.getAuthor() + ", Pages: " + book.getNoOfPages());
            }
        } else {
            // Mesaj pentru cazul în care raftul nu are cărți
            System.out.println("No books on this shelf.");
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}
