package org.example.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.authentication.data.UserEntity;
import org.example.authentication.exception.jwt.JwtTokenCreationException;
import org.example.authentication.exception.jwt.JwtTokenIsEmptyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer;
    private final Duration tokenDuration;

    public JwtService(Algorithm algorithm, JWTVerifier verifier,
                      @Value("${authentication.jwt.issuer}") String issuer,
                      @Value("${authentication.jwt.expire-in}") Duration tokenDuration) {
        this.algorithm = algorithm;
        this.verifier = verifier;
        this.issuer = issuer;
        this.tokenDuration = tokenDuration;
    }

    public String createToken(UserEntity user) {
        if (user == null) {
            throw new JwtTokenCreationException("User is null.");
        }
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(tokenDuration))
                    .withClaim("user", user.getUsername())
                    .withClaim("userId", user.getId())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withClaim("isEnabled", user.isEnabled())
                    .sign(algorithm);
        } catch (Exception exception) {
            throw new JwtTokenCreationException(exception);
        }
    }

    public UserEntity getUser(String token) {
        if (StringUtils.isBlank(token)) {
            throw new JwtTokenIsEmptyException();
        }
        DecodedJWT decodedToken = verifier.verify(token);
        return extractUserDetails(decodedToken);
    }

    private UserEntity extractUserDetails(DecodedJWT decodedToken) {
        String id = decodedToken.getClaim("userId").asString();
        String user = decodedToken.getClaim("user").asString();
        List<String> roles = decodedToken.getClaim("roles").asList(String.class);
        boolean isEnabled = BooleanUtils.toBooleanDefaultIfNull(decodedToken.getClaim("isEnabled").asBoolean(), false);
        return new UserEntity(id, user, roles, isEnabled);
    }

}
