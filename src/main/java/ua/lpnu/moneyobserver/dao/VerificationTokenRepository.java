package ua.lpnu.moneyobserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
