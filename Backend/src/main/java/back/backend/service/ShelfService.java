package back.backend.service;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.exceptions.ValidationException;
import back.backend.model.Book;
import back.backend.model.UserShelf;
import back.backend.repository.BookRepository;
import back.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Marchează clasa ca un serviciu în contextul aplicației
@Service
public class ShelfService {

    private final ShelfRepository shelfRepository; // Dependință de ShelfRepository

    // Injecție de dependență prin constructor
    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }


    // Metodă pentru a obține toate rafturile utilizatorilor
    public List<UserShelf> getAllUserShelves() {
        // Cast la listă din rezultatul iterabil al metodei findAll()
        return (List<UserShelf>) shelfRepository.findAll();
    }

    // Get a user shelf by its ID
    // Metodă pentru a obține un raft al utilizatorului după ID
    public UserShelf getUserShelfById(String id) {
        // Caută raftul în repository
        Optional<UserShelf> userShelf = shelfRepository.findById(id);
        if (userShelf.isEmpty()) {
            // Aruncă excepție dacă raftul nu este găsit
            throw new ResourceNotFoundException("Shelf not found with id: " + id);
        }
        return userShelf.get(); // Returnează raftul găsit
    }


    // Metodă pentru a crea un raft nou cu validare
    public UserShelf createUserShelf(UserShelf userShelf) {
        validateShelf(userShelf); // Validare înainte de salvare
        return shelfRepository.save(userShelf); // Salvează raftul în repository
    }


    // Metodă pentru a actualiza un raft existent cu validare
    public UserShelf updateUserShelf(String id, UserShelf updatedShelf) {
        Optional<UserShelf> userShelfOptional = shelfRepository.findById(id);
        if (userShelfOptional.isEmpty()) {
            // Aruncă excepție dacă raftul nu există
            throw new ResourceNotFoundException("Shelf not found with id: " + id);
        }

        // Validare pentru raftul actualizat
        validateShelf(updatedShelf);

        UserShelf userShelf = userShelfOptional.get();
        // Actualizează proprietățile raftului
        userShelf.setShelfName(updatedShelf.getShelfName());
        userShelf.setCapacity(updatedShelf.getCapacity());

        // Salvează modificările în repository
        return shelfRepository.save(userShelf);
    }


    // Metodă pentru a șterge un raft după ID
    public void deleteUserShelf(String id) {
        Optional<UserShelf> userShelf = shelfRepository.findById(id);
        if (userShelf.isEmpty()) {
            // Aruncă excepție dacă raftul nu este găsit
            throw new ResourceNotFoundException("Shelf not found with id: " + id);
        }
        shelfRepository.deleteById(id); // Șterge raftul din repository
    }


    // Metodă pentru a obține numărul de cărți de pe un raft
    public int getNoOfBooks(String shelfId) {
        UserShelf userShelf = getUserShelfById(shelfId); // Obține raftul
        List<Book> books = userShelf.getBooks(); // Listează cărțile
        if (books == null) {
            return 0; // Returnează 0 dacă lista de cărți este null
        }
        return books.size(); // Returnează numărul de cărți
    }


    // Metodă pentru a obține cărțile de pe un raft după ID
    public List<Book> getBooksByShelfId(String id) {
        UserShelf userShelf = getUserShelfById(id); // Obține raftul
        return userShelf.getBooks(); // Returnează lista de cărți
    }


    // Metodă pentru a valida un raft înainte de creare sau actualizare
    private void validateShelf(UserShelf userShelf) {
        if (userShelf.getShelfName() == null || userShelf.getShelfName().trim().isEmpty()) {
            // Numele raftului nu poate fi gol
            throw new ValidationException("Shelf name cannot be empty.");
        }

        if (userShelf.getCapacity() <= 0) {
            // Capacitatea trebuie să fie un număr pozitiv
            throw new ValidationException("Capacity must be a positive integer.");
        }

        // Se pot adăuga alte validări dacă este necesar
    }

}