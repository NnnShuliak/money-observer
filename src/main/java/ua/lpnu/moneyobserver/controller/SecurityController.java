package ua.lpnu.moneyobserver.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;
import ua.lpnu.moneyobserver.email.RegistrationCompleteEvent;
import ua.lpnu.moneyobserver.security.JwtCore;
import ua.lpnu.moneyobserver.service.UserService;
import ua.lpnu.moneyobserver.service.VerificationTokenService;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SecurityController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user, final HttpServletRequest request) {
        log.info("registration post method was called");
        User registredUser = userService.createNewUser(user);
        publisher.publishEvent(new RegistrationCompleteEvent(registredUser, getApplicationUrl(request)));
        return ResponseEntity.ok(registredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(user.getUsername());

        return ResponseEntity.ok( Map.of("jwtToken",jwt));


    }
    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        jwtCore.validateTokenAndGetUsername(token);
        log.info("verify get method was called");
        VerificationToken theToken = tokenService.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            log.info("active");
            return new ResponseEntity<>("User with such email already exist",HttpStatus.CONFLICT);
        }
        if (tokenService.validateAndDeleteToken(token)) {
            return ResponseEntity.ok("All good");
        }
        return new ResponseEntity<>("Invalid token",HttpStatus.BAD_REQUEST);
    }

    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
