package ua.lpnu.moneyobserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtCore {
    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.lifetime}")
    private int lifetime;


    public String generateToken(String username){

        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+lifetime))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }


    public String getNameFromJwt(String token){
        log.info("JWT token: {}", token);
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    public String validateTokenAndGetUsername(String token){
        try {
            log.info("JWT token: {}", token);
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return claimsJws.getBody().getSubject();
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            // Return null or throw an exception based on your application's requirements
            return null;
        }
    }
}
