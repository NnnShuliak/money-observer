package ua.lpnu.moneyobserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.lpnu.moneyobserver.dao.UserRepository;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.enums.Role;
import ua.lpnu.moneyobserver.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testLoadUserByUsername() {
        // Given
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        var userDetails = userService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // When / Then
       assertNull(userService.loadUserByUsername(email));

    }

    @Test
    public void testFindByEmail() {
        // Given
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        User foundUser = userService.findByEmail(email);

        // Then
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }

    @Test
    public void testCreateNewUser_UserExistsEnabled() {
        // Given
        String email = "existing@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setActive(true);
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> userService.createNewUser(existingUser));
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    public void testCreateNewUser_UserExistsDisabled() {
        // Given
        String email = "existing@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setActive(false);
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        when(userRepository.save(any())).thenReturn(existingUser);

        // When
        User savedUser = userService.createNewUser(existingUser);

        // Then
        assertNotNull(savedUser);
        assertFalse(savedUser.isActive());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testCreateNewUser_UserDoesNotExist() {
        // Given
        String email = "new@example.com";
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword("password");
        when(userRepository.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(newUser);

        // When
        User savedUser = userService.createNewUser(newUser);

        // Then
        assertNotNull(savedUser);
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(newUser);
    }
}
