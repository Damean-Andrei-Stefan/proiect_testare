package back.backend.controller;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.model.Book;
import back.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Activează configurarea automată și marchează această clasă ca un controller REST
@EnableAutoConfiguration
@RestController
@RequestMapping("/api/books") // Definește ruta de bază pentru toate cererile către acest controller
public class BookController {

    @Autowired
    private BookService service; // Injectarea serviciului pentru gestionarea cărților


    public BookController(BookService service) {
        this.service = service;

    }
    // Endpoint pentru a obține toate cărțile
    @GetMapping
    public List<Book> getAllBooks() {
        // Apelează serviciul pentru a obține toate cărțile și le returnează
        return this.service.getAllBooks();
    }

    // Endpoint pentru a obține o carte după ID
    @GetMapping("/{id}") // Mapare HTTP GET pe ruta "/api/books/{id}"
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        try {
            // Apelează serviciul pentru a obține o carte după ID
            Optional<Book> book = this.service.getBookById(id);
            return ResponseEntity.ok(book); // Returnează cartea găsită cu status HTTP 200 OK
        } catch (ResourceNotFoundException e) {
            // Dacă cartea nu este găsită, returnează un răspuns cu HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            // Dacă ID-ul este invalid, returnează un răspuns cu HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


    // Endpoint pentru a crea o carte nouă
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            boolean result = service.validateAndCreateBook(book);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            // Tratarea erorilor interne
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"A apărut o eroare internă. Vă rugăm să încercați mai târziu.\"}");
        }
    }



    // Endpoint pentru a actualiza detaliile unei cărți după titlu
    @PutMapping("/{title}") // Mapare HTTP PUT pe ruta "/api/books/{title}"
    public ResponseEntity<?> updateBook(@PathVariable String title, @RequestBody Book bookDetails) {
        try {
            // Apelează serviciul pentru a valida și actualiza cartea
            service.validateAndUpdateBook(title, bookDetails);
            return ResponseEntity.ok().build(); // Returnează HTTP 200 OK dacă actualizarea a reușit
        } catch (ResourceNotFoundException e) {
            // Dacă cartea nu este găsită, returnează HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            // Dacă datele de intrare sunt invalide, returnează HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


    // Endpoint pentru a șterge o carte după titlu
    @DeleteMapping("/{title}") // Mapare HTTP DELETE pe ruta "/api/books/{title}"
    public ResponseEntity<?> deleteBook(@PathVariable String title) {
        try {
            // Apelează serviciul pentru a șterge cartea
            service.deleteBookByTitle(title);
            return ResponseEntity.ok().build(); // Returnează HTTP 200 OK dacă ștergerea a reușit
        } catch (ResourceNotFoundException e) {
            // Dacă cartea nu este găsită, returnează HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            // Dacă titlul este invalid, returnează HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


}