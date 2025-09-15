package br.com.joao.users_service.service;

import br.com.joao.users_service.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String generateToken(User user){

        Algorithm algorithm = Algorithm.HMAC256("1234");

        try{

            return JWT.create()
                    .withIssuer("api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expiresAt(30))
                    .withClaim("role", user.getRole().name())
                    .sign(algorithm);

        } catch(JWTCreationException e){

            throw new RuntimeException("Error with JWT creation");
        }
    }

    public DecodedJWT validateToken(String token){

        try {

            Algorithm algorithm = Algorithm.HMAC256("1234");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("api")
                    .build();

            return verifier.verify(token);

        } catch(JWTVerificationException e){

            throw new RuntimeException("Error validating JWT token");
        }
    }

    private Instant expiresAt(int min) {
        return LocalDateTime.now().plusMinutes(min).toInstant(ZoneOffset.of("-03:00"));
    }
}
