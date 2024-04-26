package ua.lpnu.moneyobserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.lpnu.moneyobserver.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
