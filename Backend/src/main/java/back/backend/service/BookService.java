package back.backend.service;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.model.Book;
import back.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

// Marchează clasa ca un serviciu gestionat de Spring
@Service
public class BookService {

    private final BookRepository bookRepository; // Depozit pentru datele cărților

    // Constructor pentru injectarea dependențelor
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Returnează toate cărțile din baza de date
    public List<Book> getAllBooks() {
        return (List<Book>) this.bookRepository.findAll(); // Găsește toate cărțile
    }

    // Returnează o carte pe baza ID-ului
    public Optional<Book> getBookById(String id) {
        // Validează ID-ul
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID-ul trebuie să fie un șir de caractere valid.");
        }
        if (!id.matches("^[a-zA-Z0-9@._-]+$")) { // Permite caractere speciale comune
            throw new IllegalArgumentException("ID-ul conține caractere invalide.");
        }

        // Găsește cartea
        Optional<Book> requestedBook = this.bookRepository.findById(id);

        // Verifică dacă cartea a fost găsită, altfel aruncă o excepție
        return ofNullable(requestedBook.orElseThrow(() ->
                new ResourceNotFoundException("Cartea cu ID-ul " + id + " nu a fost găsită.")));
    }


    // Validează și actualizează detaliile unei cărți pe baza titlului
    public void validateAndUpdateBook(String title, Book bookDetails) {
        // Validează obiectul carte
        validateBookFields(bookDetails);

        // Găsește cartea după titlu
        Book book = bookRepository.findBookByBookTitle(title);

        if (book == null) {
            throw new ResourceNotFoundException("Cartea cu titlul '" + title + "' nu a fost găsită.");
        }

        // Actualizează detaliile cărții
        book.setBookTitle(bookDetails.getBookTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setNoOfPages(bookDetails.getNoOfPages());

        // Salvează modificările
        bookRepository.save(book);
    }


    // Validează și creează o carte nouă
    public boolean validateAndCreateBook(Book book) {
        // Validează obiectul carte
        validateBookFields(book);

        // Setează ID-ul unic și salvează cartea
        book.setID();
        bookRepository.save(book);
        return true;
    }


    // Metodă privată pentru validarea câmpurilor unei cărți
    private void validateBookFields(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("Obiectul carte nu poate fi null.");
        }
        if (book.getBookTitle() == null || book.getBookTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Titlul cărții este obligatoriu.");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele autorului este obligatoriu.");
        }
        if (book.getNoOfPages() <= 0) {
            throw new IllegalArgumentException("Numărul de pagini trebuie să fie un număr pozitiv.");
        }
    }


    // Șterge o carte pe baza titlului
    public void deleteBookByTitle(String title) {
        // Verifică dacă cartea există
        Book book = bookRepository.findBookByBookTitle(title);

        if (book == null) {
            throw new ResourceNotFoundException("Cartea cu titlul '" + title + "' nu a fost găsită.");
        }

        // Șterge cartea dacă a fost găsită
        bookRepository.delete(book);
    }


}


