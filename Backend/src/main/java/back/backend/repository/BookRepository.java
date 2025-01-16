package back.backend.repository;

import back.backend.model.Book;
import back.backend.model.User;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

// Interfață pentru gestionarea operațiilor CRUD pentru entitatea Book
@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    // Metodă personalizată pentru găsirea unei cărți pe baza titlului
    Book findBookByBookTitle(String title);
}
