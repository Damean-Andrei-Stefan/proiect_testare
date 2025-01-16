package back.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
// Marchează această clasă ca fiind o entitate JPA, care va fi mapată la o tabelă din baza de date
@Entity
public class Book {
    // Marchează acest câmp ca fiind cheia primară a entității
    @Id
    private String id; // ID unic pentru fiecare carte (generat automat)
    private String author;     // Numele autorului cărții
    private String bookTitle;  // Titlul cărții
    private int noOfPages;     // Numărul de pagini ale cărții
    private String shelfid;    // ID-ul raftului care leaga cartea de user

    // Constructor implicit necesar pentru JPA
    public Book() {

    }
    // Constructors, Getters, and Setters
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
    public Book(String title, String author, int numberOfPages, String shelfid) {
        this.shelfid = shelfid;
        this.id = generateUniqueId();
        this.bookTitle = title;
        this.author = author;
        this.noOfPages = numberOfPages;
        this.shelfid=shelfid;
    }

    // Getteri și Setteri
    public String  getId() {
        return id;
    }

    public void setBookTitle(String title) {
        this.bookTitle = title;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setID(){
        this.id = generateUniqueId();
    }
    public void setNoOfPages(int numberOfPages) {
        this.noOfPages = numberOfPages;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setId(String id){
        this.id = id;
    }

}
