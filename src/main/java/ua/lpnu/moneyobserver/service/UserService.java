package ua.lpnu.moneyobserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.lpnu.moneyobserver.domain.User;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User findByEmail(String email);

    User createNewUser(User user);
}
