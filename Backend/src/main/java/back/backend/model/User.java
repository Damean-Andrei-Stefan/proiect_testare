package back.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// Marchează această clasă ca o entitate JPA, care se mapează la o tabelă în baza de date
@Entity
public class User {
    // Marchează acest câmp ca fiind cheia primară a tabelei
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generează automat valori unice pentru cheia primară
     private Long userid; // Identificator unic pentru fiecare utilizator
    private String username; // Stochează numele de utilizator al utilizatorului
    private String email;    // Stochează adresa de e-mail a utilizatorului
    private String password; // Stochează parola utilizatorului


    // Constructor parametrizat pentru inițializarea obiectelor User cu valori specifice
    public User(String username, String password, String email) {
        this.username = username; // Atribuie numele de utilizator câmpului
        this.password = password; // Atribuie parola câmpului
        this.email = email;       // Atribuie adresa de e-mail câmpului
    }


    // Constructor implicit (fără argumente) necesar pentru JPA
    public User() {
    }

    // Setter pentru câmpul userid
    public void setId(Long id) {
        this.userid = id;
    }

    // Getter pentru câmpul userid
    public Long getId() {
        return userid;
    }

    // Getter pentru câmpul username
    public String getUsername() {
        return username;
    }

    // Setter pentru câmpul username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter pentru câmpul password
    public String getPassword() {
        return password;
    }

    // Setter pentru câmpul password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter pentru câmpul email
    public String getEmail() {
        return email;
    }

    // Setter pentru câmpul email
    public void setEmail(String email) {
        this.email = email;
    }

}