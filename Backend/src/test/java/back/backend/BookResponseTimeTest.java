package back.backend;

import back.backend.controller.BookController;
import back.backend.model.Book;
import back.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookResponseTimeTest {

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
    public void testGetAllBooks_ResponseTime() throws Exception {
        // Simulate 1000 books in the database
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            books.add(new Book("Book Title " + i, "Author " + i, 200 + i, "shelf-" + (i % 10)));
        }

        when(bookService.getAllBooks()).thenReturn(books);

        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("Response time for fetching 1000 books: " + responseTime + " ms");
    }
}
