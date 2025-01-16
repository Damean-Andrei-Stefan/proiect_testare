package back.backend.service;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.exceptions.ValidationException;
import back.backend.model.User;
import back.backend.repository.UserRepository;
import back.backend.tokens.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

// Marchează clasa ca serviciu în contextul aplicației
@Service
public class UserService {

    private final UserRepository userRepository; // Depozit pentru datele utilizatorilor
    private final JWTUtil jwtUtil; // Utilitar pentru generarea și validarea token-urilor

    // Constructor pentru injectarea dependențelor
    @Autowired
    public UserService(UserRepository userRepository, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    // Înregistrează un utilizator nou și validează datele
    public void registerUser(User user) {
        validateUser(user); // Validare înainte de salvare
        userRepository.save(user); // Salvează utilizatorul în baza de date
    }


    // Autentifică utilizatorul și generează un token dacă acreditările sunt valide
    public String loginUser(User user) {
        validateLoginCredentials(user); // Validare a acreditărilor
        return jwtUtil.generateToken(user.getUsername()); // Generează token-ul JWT pentru utilizator
    }


    // Validează datele utilizatorului la înregistrare
    private void validateUser(User user) {
        // Verifică dacă numele de utilizator este gol sau null
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ValidationException("Username este obligatoriu");
        }
        // Verifică dacă email-ul este gol sau null
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email este obligatoriu");
        }
        // Verifică dacă parola este goală sau null
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidationException("Parola este obligatorie");
        }
    }


    // Validează acreditările utilizatorului la login
    private void validateLoginCredentials(User user) {
        // Caută utilizatorul după numele de utilizator
        User foundUser = userRepository.findByUsername(user.getUsername());
        // Verifică dacă utilizatorul există și dacă parola corespunde
        if (foundUser == null || !Objects.equals(foundUser.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Acreditări invalide");
        }
    }

}
