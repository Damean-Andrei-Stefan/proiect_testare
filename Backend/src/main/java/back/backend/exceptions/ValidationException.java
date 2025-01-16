package back.backend.exceptions;

// Excepție personalizată pentru validare
public class ValidationException extends RuntimeException {

    // Constructor care acceptă un mesaj detaliat pentru eroarea de validare
    public ValidationException(String message) {
        super(message); // Apelează constructorul clasei părinte RuntimeException cu mesajul specific
    }
}
