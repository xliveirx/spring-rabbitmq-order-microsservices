package br.com.joao.orders_service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

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

}
