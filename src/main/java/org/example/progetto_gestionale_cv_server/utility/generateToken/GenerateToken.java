package org.example.progetto_gestionale_cv_server.utility.generateToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class GenerateToken {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 1 giorno in millisecondi

    public String tokenGeneration(Users user) {
        return Jwts.builder()
                .setSubject(user.getNome())
                .setId(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}
