package ua.lpnu.moneyobserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.moneyobserver.dao.VerificationTokenRepository;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;
import ua.lpnu.moneyobserver.service.VerificationTokenService;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final int EXPIRATION_TIME = 15;

    private final VerificationTokenRepository repository;

    @Override
    public boolean validateAndDeleteToken(String theToken) {
        VerificationToken token = repository.findByToken(theToken);
        if (token == null) {
            return false;
        }
        User user = token.getUser();

        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            repository.delete(token);
            return false;
        }
        user.setActive(true);
        repository.save(token);

        repository.delete(token);
        return true;
    }

    @Override
    public VerificationToken findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public VerificationToken createNewTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = repository.findByUser(user);
        if (verificationToken != null) {
            verificationToken.setToken(token);
            verificationToken.setExpirationTime(getTokenExpirationTime());
            return repository.save(verificationToken);
        }
        VerificationToken newVerificationToken = new VerificationToken();
        newVerificationToken.setToken(token);
        newVerificationToken.setUser(user);
        newVerificationToken.setExpirationTime(getTokenExpirationTime());
        return repository.save(newVerificationToken);
    }

    @Override
    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
