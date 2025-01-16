package back.backend.model;

import back.backend.model.Book;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

// Clasa BookRowMapper implementează RowMapper pentru a mapă rândurile dintr-un ResultSet la obiecte de tip Book
public class BookRowMapper implements RowMapper<Book> {
    // Suprascrierea metodei mapRow pentru a mapa un rând SQL la un obiect Book
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book(); // Creează un obiect gol de tip Book
        // Setează proprietățile obiectului Book folosind datele din ResultSet
        book.setId(rs.getString("id"));                 // Atribuie valoarea coloanei "id" câmpului id
        book.setBookTitle(rs.getString("bookTitle"));   // Atribuie valoarea coloanei "bookTitle" câmpului bookTitle
        book.setAuthor(rs.getString("author"));         // Atribuie valoarea coloanei "author" câmpului author
        book.setNoOfPages(rs.getInt("noOfPages"));      // Atribuie valoarea coloanei "noOfPages" câmpului noOfPages

        // Returnează obiectul Book populat
        return book;

    }
}