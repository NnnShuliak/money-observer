package ua.lpnu.moneyobserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<?> getUserName(Principal principal) {
        String username = userService.findByEmail(principal.getName()).getName();

        return ResponseEntity.ok(Map.of("username",username));
    }



}
