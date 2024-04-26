package ua.lpnu.moneyobserver.service;

import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;

import java.util.Date;

public interface VerificationTokenService {
    boolean validateAndDeleteToken(String theToken);

    VerificationToken findByToken(String token);

    VerificationToken createNewTokenForUser(User user);

    Date getTokenExpirationTime();
}
