package org.example.authentication.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.TokenDto;
import org.example.authentication.security.JwtService;
import org.example.authentication.security.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtService jwtService;
    private final LoginService loginService;

    public UserController(JwtService jwtService, LoginService loginService) {
        this.jwtService = jwtService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public void register() {

    }

    @PostMapping("/login")
    public TokenDto login(HttpServletRequest request) {
        String jwt = loginService.login(request);
        return new TokenDto(jwt);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@RequestBody TokenDto tokenDto) {
        jwtService.verifyJWT(tokenDto.getJwt());
    }



}
