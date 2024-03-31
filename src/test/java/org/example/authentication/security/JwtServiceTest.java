package org.example.authentication.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.authentication.data.UserEntity;
import org.example.authentication.exception.JwtTokenCreationException;
import org.example.authentication.exception.JwtTokenIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.authentication.builder.EntityBuilder.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    JWTVerifier verifier;

    JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(Algorithm.none(), verifier, "issuer");
    }

    @Test
    @DisplayName("when user is provided then generate jwt")
    void createTokenTest1() {
        //given
        UserEntity user = createUser().build();

        //when
        String token = jwtService.createToken(user);

        //then
        assertNotNull(token);
    }
    @Test
    @DisplayName("when user is null then throw exception")
    void createTokenTest2() {
        //when then
        assertThrows(JwtTokenCreationException.class, () -> jwtService.createToken(null));
    }

    @Test
    @DisplayName("when token is provided then return user")
    void getUserTest1() {
        //given
        String token = "token";
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        given(verifier.verify(token)).willReturn(decodedJWT);
        Claim claimMock = mock();
        given(decodedJWT.getClaim("userId")).willReturn(claimMock);
        given(decodedJWT.getClaim("user")).willReturn(claimMock);
        given(decodedJWT.getClaim("roles")).willReturn(claimMock);
        given(decodedJWT.getClaim("isEnabled")).willReturn(claimMock);

        //when
        UserEntity userEntity = assertDoesNotThrow(() -> jwtService.getUser(token));

        //then
        assertNotNull(userEntity);

    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("when token is null or empty then throw exception")
    void getUserTest2(String token) {
        //when then
        assertThrows(JwtTokenIsEmptyException.class, () -> jwtService.getUser(token));
    }

}