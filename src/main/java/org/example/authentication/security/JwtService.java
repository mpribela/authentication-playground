package org.example.authentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.BooleanUtils;
import org.example.authentication.data.UserEntity;
import org.example.authentication.exception.JwtTokenCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${classpath:keys/public_key.pem}") RSAPublicKey rsaPublicKey,
                      @Value("${classpath:keys/private_key.pem}") RSAPrivateKey rsaPrivateKey) {
        this.algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer("myIssuer")
                .build();
    }

    public String createJWT(UserEntity user) {
        try {
            return JWT.create()
                    .withIssuer("myIssuer")
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(5, MINUTES))
                    .withClaim("user", user.getUsername())
                    .withClaim("userId", user.getId())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withClaim("isEnabled", user.isEnabled())
                    .sign(algorithm);
        } catch (Exception exception) {
            throw new JwtTokenCreationException(exception);
        }
    }

    public void verifyJWT(String jwt) {
        verifier.verify(jwt);
    }

    public UserEntity extractUserDetails(String jwt) {
        DecodedJWT decoded = JWT.decode(jwt);
        String id = decoded.getClaim("userId").asString();
        String user = decoded.getClaim("user").asString();
        List<String> roles = decoded.getClaim("roles").asList(String.class);
        boolean isEnabled = BooleanUtils.toBooleanDefaultIfNull(decoded.getClaim("user").asBoolean(), false);
        return new UserEntity(id, user, roles, isEnabled);
    }

}
