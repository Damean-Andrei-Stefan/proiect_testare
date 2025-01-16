package back.backend;

import back.backend.controller.UserController;
import back.backend.exceptions.ResourceNotFoundException;
import back.backend.exceptions.ValidationException;
import back.backend.model.User;
import back.backend.service.UserService;
import back.backend.tokens.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User newUser = new User("username", "password", "email@example.com");

        // Simulate successful registration
        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content("{\"username\":\"username\", \"password\":\"password\", \"email\":\"email@example.com\"}"))
                .andExpect(status().isCreated())  // 201 Created status
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    public void testRegisterUser_Failure_BadRequest() throws Exception {
        User invalidUser = new User("", "password", "email@example.com");

        // Simulate validation failure
        doThrow(new ValidationException("Username cannot be empty")).when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content("{\"username\":\"\", \"password\":\"password\", \"email\":\"email@example.com\"}"))
                .andExpect(status().isBadRequest())  // 400 Bad Request status
                .andExpect(jsonPath("$.error").value("Username cannot be empty"));
    }


    @Test
    public void testLoginUser_Success() throws Exception {
        User existingUser = new User("username", "password", "email@example.com");
        String token = "mockToken";

        // Simulate successful login
        when(userService.loginUser(any(User.class))).thenReturn(token);

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"username\":\"username\", \"password\":\"password\", \"email\":\"email@example.com\"}"))
                .andExpect(status().isOk())  // 200 OK status
                .andExpect(jsonPath("$.token").value(token));  // Return token in response
    }

    @Test
    public void testLoginUser_Failure_UserNotFound() throws Exception {
        User invalidUser = new User("wrongUsername", "wrongPassword", "wrongEmail@example.com");

        // Simulate user not found scenario
        when(userService.loginUser(any(User.class))).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"username\":\"wrongUsername\", \"password\":\"wrongPassword\", \"email\":\"wrongEmail@example.com\"}"))
                .andExpect(status().isNotFound())  // 404 Not Found status
                .andExpect(jsonPath("$.error").value("User not found"));
    }
}
