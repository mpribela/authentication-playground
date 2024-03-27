package org.example.authentication.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfiguration {

    @Bean
    public Algorithm algorithm(@Value("${classpath:keys/public_key.pem}") RSAPublicKey rsaPublicKey,
                               @Value("${classpath:keys/private_key.pem}") RSAPrivateKey rsaPrivateKey) {
        return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }

    @Bean
    public JWTVerifier verifier(@Value("${authentication.jwt.issuer}") String issuer, Algorithm algorithm) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }
}
