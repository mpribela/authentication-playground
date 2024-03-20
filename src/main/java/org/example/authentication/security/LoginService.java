package org.example.authentication.security;

import jakarta.servlet.http.HttpServletRequest;
import org.example.authentication.data.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BasicAuthenticationConverter authenticationConverter;

    public LoginService(JwtService jwtService, AuthenticationManager authenticationManager,
                        BasicAuthenticationConverter authenticationConverter) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
    }

    public String login(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtService.createJWT((UserEntity) authentication.getPrincipal());
    }

}
