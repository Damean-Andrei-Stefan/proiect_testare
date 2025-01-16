package back.backend.controller;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.model.UserShelf;
import back.backend.service.ShelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Marchează clasa ca un controller REST și definește ruta de bază "/api/user-shelves"
@RestController
@RequestMapping("/api/user-shelves")
public class ShelfController {

    private final ShelfService shelfService; // Serviciul pentru gestionarea rafturilor

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    // Get all user shelves
    // Endpoint pentru a obține toate rafturile utilizatorului
    @GetMapping
    public List<UserShelf> getAllUserShelves() {
        // Apelează serviciul pentru a returna toate rafturile
        return shelfService.getAllUserShelves();
    }

    // Endpoint pentru a obține un raft utilizator după ID
    @GetMapping("/{id}") // Ruta cu variabilă de cale {id}
    public ResponseEntity<?> getUserShelfById(@PathVariable String id) {
        try {
            // Apelează serviciul pentru a găsi raftul după ID
            UserShelf userShelf = shelfService.getUserShelfById(id);
            return ResponseEntity.ok(userShelf); // Returnează raftul cu HTTP 200 OK
        } catch (ResourceNotFoundException ex) {
            // Returnează HTTP 404 Not Found dacă raftul nu este găsit
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        }
    }


    // Endpoint pentru a crea un raft nou
    @PostMapping
    public ResponseEntity<?> createUserShelf(@RequestBody UserShelf userShelf) {
        try {
            // Apelează serviciul pentru a crea un raft nou
            UserShelf createdShelf = shelfService.createUserShelf(userShelf);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdShelf); // Returnează raftul creat cu HTTP 201 Created
        } catch (IllegalArgumentException ex) {
            // Returnează HTTP 400 Bad Request dacă datele sunt invalide
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        }
    }


    // Endpoint pentru a actualiza un raft existent
    @PutMapping("/{id}") // Ruta cu variabilă de cale {id}
    public ResponseEntity<?> updateUserShelf(@PathVariable String id, @RequestBody UserShelf updatedShelf) {
        try {
            // Apelează serviciul pentru a actualiza raftul
            UserShelf userShelf = shelfService.updateUserShelf(id, updatedShelf);
            return ResponseEntity.ok(userShelf); // Returnează raftul actualizat cu HTTP 200 OK
        } catch (ResourceNotFoundException ex) {
            // Returnează HTTP 404 Not Found dacă raftul nu este găsit
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            // Returnează HTTP 400 Bad Request dacă datele sunt invalide
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        }
    }


    // Endpoint pentru a șterge un raft după ID
    @DeleteMapping("/{id}") // Ruta cu variabilă de cale {id}
    public ResponseEntity<?> deleteUserShelf(@PathVariable String id) {
        try {
            // Apelează serviciul pentru a șterge raftul
            shelfService.deleteUserShelf(id);
            return ResponseEntity.ok().build(); // Returnează HTTP 200 OK dacă ștergerea a reușit
        } catch (ResourceNotFoundException ex) {
            // Returnează HTTP 404 Not Found dacă raftul nu este găsit
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        }
    }


    // Clasa internă pentru gestionarea răspunsurilor de eroare
    public static class ErrorResponse {
        private String message; // Mesajul de eroare

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
