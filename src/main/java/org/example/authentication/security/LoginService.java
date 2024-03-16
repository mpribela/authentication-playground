package org.example.authentication.security;

import jakarta.servlet.http.HttpServletRequest;
import org.example.authentication.data.UserEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    public LoginService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String login(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (!authenticate.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        UserEntity user = userService.getUserEntityByUsername(authenticationToken.getName());
        if (!user.isEnabled()) {
            throw new AccessDeniedException("User is disabled.");
        }
        return jwtService.createJWT(user);
    }

}
