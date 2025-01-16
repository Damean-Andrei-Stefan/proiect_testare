package back.backend;
import back.backend.controller.BookController;
import back.backend.exceptions.ResourceNotFoundException;
import back.backend.model.Book;
import back.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testGetBookById_ThrowsException_WhenNotFound() throws Exception {
        String bookId = "invalid-id";
        when(bookService.getBookById(bookId)).thenThrow(new ResourceNotFoundException("Book with ID " + bookId + " not found."));

        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID invalid-id not found."));
    }

    @Test
    public void testGetBookById_ReturnsBook_WhenFound() throws Exception {
        Book book = new Book("Book Title 1", "Author 1", 300, "shelf-1");
        when(bookService.getBookById(book.getId())).thenReturn(java.util.Optional.of(book));

        mockMvc.perform(get("/api/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("Book Title 1"))
                .andExpect(jsonPath("$.author").value("Author 1"));
    }

    @Test
    public void testCreateBook_ThrowsException_WhenInvalidBook() throws Exception {
        // Create an invalid book (e.g., empty book title)
        Book invalidBook = new Book("", "Author 1", 300, "shelf-1");

        // Simulate the service throwing an exception when trying to create the invalid book
        when(bookService.validateAndCreateBook(any(Book.class))).thenThrow(new IllegalArgumentException("Book title must be provided."));

        // Perform the request with invalid data
        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content("{\"bookTitle\":\"\", \"author\":\"Author 1\", \"noOfPages\":300, \"shelfid\":\"shelf-1\"}"))
                .andExpect(status().isBadRequest()) // Expect 400 status for bad request
                .andExpect(jsonPath("$.message").value("Book title must be provided.")); // Expect error message
    }


    @Test
    public void testCreateBook_Success() throws Exception {
        Book validBook = new Book("Book Title 1", "Author 1", 300, "shelf-1");
        when(bookService.validateAndCreateBook(any(Book.class))).thenReturn(true);

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content("{\"bookTitle\":\"Book Title 1\", \"author\":\"Author 1\", \"noOfPages\":300, \"shelfid\":\"shelf-1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateBook_ThrowsException_WhenBookNotFound() throws Exception {
        String title = "Nonexistent Book";

        // Simulate the service throwing an exception when trying to update a nonexistent book
        doThrow(new ResourceNotFoundException("Book with title '" + title + "' not found."))
                .when(bookService).validateAndUpdateBook(eq(title), any(Book.class));

        mockMvc.perform(put("/api/books/" + title)
                        .contentType("application/json")
                        .content("{\"bookTitle\":\"Updated Title\", \"author\":\"Updated Author\", \"noOfPages\":350, \"shelfid\":\"shelf-1\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with title 'Nonexistent Book' not found."));
    }

    @Test
    public void testUpdateBook_Success() throws Exception {
        // Simulate a valid book object to be updated
        Book updatedBook = new Book("Updated Title", "Updated Author", 350, "shelf-1");

        // Simulate that the book is successfully updated (the method does not return anything)
        doNothing().when(bookService).validateAndUpdateBook(eq("Book Title 1"), any(Book.class));

        mockMvc.perform(put("/api/books/Book Title 1")
                        .contentType("application/json")
                        .content("{\"bookTitle\":\"Updated Title\", \"author\":\"Updated Author\", \"noOfPages\":350, \"shelfid\":\"shelf-1\"}"))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeleteBook_ThrowsException_WhenBookNotFound() throws Exception {
        String title = "Nonexistent Book";
        // Simulate the service throwing an exception when trying to delete a nonexistent book
        doThrow(new ResourceNotFoundException("Book with title '" + title + "' not found."))
                .when(bookService).deleteBookByTitle(eq(title));

        mockMvc.perform(delete("/api/books/" + title))
                .andExpect(status().isNotFound())  // Expecting 404
                .andExpect(jsonPath("$.message").value("Book with title 'Nonexistent Book' not found."));  // Ensure the message is returned
    }

    @Test
    public void testDeleteBook_Success() throws Exception {
        String title = "Book Title 1";
        doNothing().when(bookService).deleteBookByTitle(title);

        mockMvc.perform(delete("/api/books/" + title))
                .andExpect(status().isOk());
    }
}
