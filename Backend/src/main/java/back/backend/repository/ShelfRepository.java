package back.backend.repository;

import back.backend.model.Book;
import back.backend.model.User;
import back.backend.model.UserShelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

// Marchează interfața ca repository, indicând că gestionează datele pentru UserShelf
@Repository
public interface ShelfRepository extends CrudRepository<UserShelf, String> {
    // Extinde CrudRepository pentru a obține operații standard (create, read, update, delete) pe entitatea UserShelf.
}


