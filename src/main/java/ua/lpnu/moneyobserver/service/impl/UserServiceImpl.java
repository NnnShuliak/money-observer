package ua.lpnu.moneyobserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.lpnu.moneyobserver.dao.UserRepository;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.enums.Role;
import ua.lpnu.moneyobserver.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User createNewUser(User user) {
        User foundUser = repository.findByEmail(user.getEmail());
        if (foundUser == null) {
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        }
        if (foundUser.isEnabled()) throw new IllegalArgumentException("User already exist");
        foundUser.setName(user.getName());
        foundUser.setRole(Role.USER);
        foundUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(foundUser);
    }

}
