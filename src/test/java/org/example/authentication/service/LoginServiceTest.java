package org.example.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.authentication.builder.EntityBuilder;
import org.example.authentication.data.UserEntity;
import org.example.authentication.exception.jwt.JwtTokenCreationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    BasicAuthenticationConverter authenticationConverter;
    @InjectMocks
    LoginService service;

    @Mock
    HttpServletRequest request;
    String jwt = "jwt";

    @Test
    @DisplayName("when user is authenticated then return jwt token")
    void loginTest1() {
        //given
        var authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        var authenticationResult = mock(Authentication.class);
        given(authenticationConverter.convert(request)).willReturn(authenticationToken);
        given(authenticationManager.authenticate(authenticationToken)).willReturn(authenticationResult);
        UserEntity user = EntityBuilder.createUser().enabled(true).build();
        given(authenticationResult.getPrincipal()).willReturn(user);
        given(jwtService.createToken(user)).willReturn(jwt);

        //when
        String result = service.login(request);

        //then
        assertEquals(jwt, result);
    }

    @Test
    @DisplayName("when user is not authenticated then throw AuthenticationException")
    void loginTest2() {
        //given
        var authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        given(authenticationConverter.convert(request)).willReturn(authenticationToken);
        given(authenticationManager.authenticate(authenticationToken)).willThrow(DisabledException.class);

        //when then
        assertThrows(DisabledException.class, () -> service.login(request));
    }

    @Test
    @DisplayName("when authentication converted return null then throw AuthenticationException")
    void loginTest3() {
        //given
        given(authenticationConverter.convert(request)).willReturn(null);
        given(authenticationManager.authenticate(isNull())).willThrow(UsernameNotFoundException.class);

        //when then
        assertThrows(UsernameNotFoundException.class, () -> service.login(request));
    }

    @Test
    @DisplayName("when authentication converted throws exception then propagate the exception")
    void loginTest4() {
        //given
        given(authenticationConverter.convert(request)).willThrow(AccountExpiredException.class);

        //when then
        assertThrows(AccountExpiredException.class, () -> service.login(request));
    }


    @Test
    @DisplayName("when jwt token generation throws exception then propagate the exception")
    void loginTest5() {
        //given
        var authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        var authenticationResult = mock(Authentication.class);
        given(authenticationConverter.convert(request)).willReturn(authenticationToken);
        given(authenticationManager.authenticate(authenticationToken)).willReturn(authenticationResult);
        UserEntity user = EntityBuilder.createUser().enabled(true).build();
        given(authenticationResult.getPrincipal()).willReturn(user);
        given(jwtService.createToken(user)).willThrow(JwtTokenCreationException.class);

        //when then
        assertThrows(JwtTokenCreationException.class, () -> service.login(request));
    }
}