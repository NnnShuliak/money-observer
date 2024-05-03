package ua.lpnu.moneyobserver.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.lpnu.moneyobserver.dao.VerificationTokenRepository;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;
import ua.lpnu.moneyobserver.service.impl.VerificationTokenServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VerificationTokenServiceTest {

    @MockBean
    private VerificationTokenRepository repository;

    @Autowired
    private VerificationTokenService service;

    @Test
    public void testValidateAndDeleteToken_ValidToken() {
        // Given
        String tokenValue = "validToken";
        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        token.setUser(new User());
        token.setExpirationTime(new Date(System.currentTimeMillis() + 1000)); // Future expiration time
        when(repository.findByToken(tokenValue)).thenReturn(token);

        // When
        boolean result = service.validateAndDeleteToken(tokenValue);

        // Then
        assertTrue(result);
        verify(repository, times(1)).delete(token);
    }

    @Test
    public void testValidateAndDeleteToken_ExpiredToken() {
        // Given
        String tokenValue = "expiredToken";
        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        token.setUser(new User());
        token.setExpirationTime(new Date(System.currentTimeMillis() - 1000)); // Past expiration time
        when(repository.findByToken(tokenValue)).thenReturn(token);

        // When
        boolean result = service.validateAndDeleteToken(tokenValue);

        // Then
        assertFalse(result);
        verify(repository, times(1)).delete(token);
    }

    @Test
    public void testFindByToken() {
        // Given
        String tokenValue = "token";
        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        when(repository.findByToken(tokenValue)).thenReturn(token);

        // When
        VerificationToken result = service.findByToken(tokenValue);

        // Then
        assertNotNull(result);
        assertEquals(tokenValue, result.getToken());
    }

    @Test
    public void testCreateNewTokenForUser() {
        // Given
        User user = new User();
        VerificationToken existingToken = new VerificationToken();
        existingToken.setToken("existingToken");
        existingToken.setUser(user);
        existingToken.setExpirationTime(new Date(System.currentTimeMillis() + 1000)); // Future expiration time
        when(repository.findByUser(user)).thenReturn(existingToken);

        // When
        VerificationToken result = service.createNewTokenForUser(user);

        // Then
        verify(repository, times(1)).save(existingToken);
    }

    @Test
    public void testCreateNewTokenForUser_NoExistingToken() {
        // Given
        User user = new User();
        when(repository.findByUser(user)).thenReturn(null);


        // When
        VerificationToken result = service.createNewTokenForUser(user);


        verify(repository, times(1)).save(any());
    }


}
