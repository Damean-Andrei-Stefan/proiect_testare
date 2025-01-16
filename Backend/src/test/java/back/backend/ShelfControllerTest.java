package back.backend;

import back.backend.controller.ShelfController;
import back.backend.exceptions.ResourceNotFoundException;
import back.backend.model.UserShelf;
import back.backend.service.ShelfService;
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

public class ShelfControllerTest {

    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfController shelfController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shelfController).build();
    }

    @Test
    public void testGetAllUserShelves() throws Exception {
        UserShelf shelf1 = new UserShelf("Shelf 1", 10);
        UserShelf shelf2 = new UserShelf("Shelf 2", 20);
        when(shelfService.getAllUserShelves()).thenReturn(Arrays.asList(shelf1, shelf2));

        mockMvc.perform(get("/api/user-shelves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shelfName").value("Shelf 1"))
                .andExpect(jsonPath("$[1].shelfName").value("Shelf 2"));
    }

    @Test
    public void testGetUserShelfById_ThrowsException_WhenNotFound() throws Exception {
        String shelfId = "invalid-id";
        when(shelfService.getUserShelfById(shelfId)).thenThrow(new ResourceNotFoundException("Shelf with ID " + shelfId + " not found."));

        mockMvc.perform(get("/api/user-shelves/" + shelfId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shelf with ID invalid-id not found."));
    }

    @Test
    public void testGetUserShelfById_ReturnsShelf_WhenFound() throws Exception {
        UserShelf shelf = new UserShelf("Shelf 1", 10);
        shelf.setId("1");
        when(shelfService.getUserShelfById(shelf.getId())).thenReturn(shelf);

        mockMvc.perform(get("/api/user-shelves/" + shelf.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelfName").value("Shelf 1"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    public void testCreateUserShelf_ThrowsException_WhenInvalidShelf() throws Exception {
        UserShelf invalidShelf = new UserShelf("", -1);

        // Simulate the service throwing an exception when trying to create the invalid shelf
        when(shelfService.createUserShelf(any(UserShelf.class))).thenThrow(new IllegalArgumentException("Invalid shelf data."));

        mockMvc.perform(post("/api/user-shelves")
                        .contentType("application/json")
                        .content("{\"shelfName\":\"\", \"capacity\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid shelf data."));
    }

    @Test
    public void testCreateUserShelf_Success() throws Exception {
        UserShelf validShelf = new UserShelf("Shelf 1", 10);
        when(shelfService.createUserShelf(any(UserShelf.class))).thenReturn(validShelf);

        mockMvc.perform(post("/api/user-shelves")
                        .contentType("application/json")
                        .content("{\"shelfName\":\"Shelf 1\", \"capacity\":10}"))
                .andExpect(status().isCreated()) // Expect 201 status for resource creation
                .andExpect(jsonPath("$.shelfName").value("Shelf 1"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    public void testUpdateUserShelf_ThrowsException_WhenNotFound() throws Exception {
        String shelfId = "invalid-id";
        UserShelf updatedShelf = new UserShelf("Updated Shelf", 15);
        when(shelfService.updateUserShelf(eq(shelfId), any(UserShelf.class)))
                .thenThrow(new ResourceNotFoundException("Shelf with ID " + shelfId + " not found."));

        mockMvc.perform(put("/api/user-shelves/" + shelfId)
                        .contentType("application/json")
                        .content("{\"shelfName\":\"Updated Shelf\", \"capacity\":15}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shelf with ID invalid-id not found."));
    }

    @Test
    public void testUpdateUserShelf_Success() throws Exception {
        UserShelf shelf = new UserShelf("Shelf 1", 15);
        shelf.setId("1");
        when(shelfService.updateUserShelf(eq(shelf.getId()), any(UserShelf.class))).thenReturn(shelf);

        mockMvc.perform(put("/api/user-shelves/" + shelf.getId())
                        .contentType("application/json")
                        .content("{\"shelfName\":\"Shelf 1\", \"capacity\":15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelfName").value("Shelf 1"))
                .andExpect(jsonPath("$.capacity").value(15));
    }

    @Test
    public void testDeleteUserShelf_ThrowsException_WhenNotFound() throws Exception {
        String shelfId = "invalid-id";
        doThrow(new ResourceNotFoundException("Shelf with ID " + shelfId + " not found."))
                .when(shelfService).deleteUserShelf(shelfId);

        mockMvc.perform(delete("/api/user-shelves/" + shelfId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shelf with ID invalid-id not found."));
    }

    @Test
    public void testDeleteUserShelf_Success() throws Exception {
        String shelfId = "1";
        doNothing().when(shelfService).deleteUserShelf(shelfId);

        mockMvc.perform(delete("/api/user-shelves/" + shelfId))
                .andExpect(status().isOk());
    }
}
