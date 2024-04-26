package ua.lpnu.moneyobserver.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
}
