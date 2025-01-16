
package back.backend.repository;

import back.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Interfața responsabilă pentru gestionarea operațiilor de acces la baza de date pentru entitatea User
@Repository

public interface UserRepository extends CrudRepository<User, Long> {

    // Găsește un utilizator după username
    public User findByUsername(String email);
}



