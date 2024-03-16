package org.example.authentication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.authentication.data.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public final static String USER_ID_ATTRIBUTE = "USER_ID";
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isBlank(authorizationHeader) || !StringUtils.startsWith(authorizationHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
        }
        String token = StringUtils.removeStart(authorizationHeader, BEARER_PREFIX);
        jwtService.verifyJWT(token);
        UserEntity user = jwtService.extractUserDetails(token);
        request.setAttribute(USER_ID_ATTRIBUTE, user.getId());
        setSecurityContext(user);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return StringUtils.equals(request.getRequestURI(), "/user/login");
    }

    private void setSecurityContext(UserDetails user) {
        UsernamePasswordAuthenticationToken successAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), user.getAuthorities());
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(successAuthentication);
        this.securityContextHolderStrategy.setContext(context);
    }
}
