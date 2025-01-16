package back.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Anotare care mapează această excepție la codul de status HTTP 404 (Not Found)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Constructor care acceptă detalii despre resursa care nu a fost găsită
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Creează un mesaj detaliat, formatat în stilul "Resursa nu a fost găsită cu acest criteriu"
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }

    // Constructor alternativ care acceptă doar numele resursei
    public ResourceNotFoundException(String resourceName) {
        // Creează un mesaj simplu bazat pe numele resursei
        super(String.format("%s", resourceName));
    }
}

