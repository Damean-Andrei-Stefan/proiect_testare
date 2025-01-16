
package back.backend.controller;

import back.backend.exceptions.ResourceNotFoundException;
import back.backend.exceptions.ValidationException;
import back.backend.model.User;
import back.backend.repository.UserRepository;
import back.backend.service.UserService;
import back.backend.tokens.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import java.util.Objects;
// Activează configurarea automată și marchează această clasă ca un controller REST
@EnableAutoConfiguration
@RestController
public class UserController {
    // Dependința către UserService, injectată automat de Spring
    @Autowired
    private final UserService userService;
    // Constructor pentru injectarea UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint pentru înregistrarea unui utilizator nou
    @PostMapping("/register") // Mapare HTTP POST pe ruta "/register"
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Apelează serviciul pentru înregistrarea utilizatorului
            userService.registerUser(user);
            // Returnează un răspuns cu status HTTP 201 (CREATED) și un mesaj de succes
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("message", "User registered successfully"));
        } catch (ValidationException e) {
            // În cazul unei erori de validare, returnează un răspuns cu status HTTP 400 (BAD REQUEST)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Endpoint pentru autentificarea unui utilizator
    @PostMapping("/login") // Mapare HTTP POST pe ruta "/login"
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            // Apelează serviciul pentru autentificarea utilizatorului și obține un token JWT
            String token = userService.loginUser(user);
            // Returnează un răspuns cu status HTTP 200 (OK) și token-ul JWT
            return ResponseEntity.ok()
                    .body(Collections.singletonMap("token", token));
        } catch (ResourceNotFoundException e) {
            // În cazul în care utilizatorul nu este găsit, returnează un răspuns cu status HTTP 404 (NOT FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}


