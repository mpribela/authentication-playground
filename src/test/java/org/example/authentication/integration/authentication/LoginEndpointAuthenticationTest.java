package org.example.authentication.integration.authentication;

import org.example.authentication.data.UserEntity;
import org.example.authentication.dto.TokenDto;
import org.example.authentication.integration.base.AuthenticationBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.authentication.builder.EntityBuilder.createUser;

public class LoginEndpointAuthenticationTest extends AuthenticationBase {

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Nested
    class Login {

        @Test
        @DisplayName("when user exist then user can login in with credentials")
        void test() {
            //given
            UserEntity user = createUser().username("user").build();
            userRepository.save(user);

            //when
            ResponseEntity<TokenDto> response = testRestTemplate
                    .withBasicAuth(user.getUsername(), user.getPassword())
                    .postForEntity("/user/login", null, TokenDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getJwt()).isNotBlank();
        }

        @Test
        @DisplayName("when credentials are invalid then no token is returned")
        void test2() {
            //given
            UserEntity user = createUser().username("user").password("pass").build();
            userRepository.save(user);

            //when
            ResponseEntity<TokenDto> response = testRestTemplate
                    .withBasicAuth(user.getUsername(), "invalid_password")
                    .postForEntity("/user/login", null, TokenDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }
}
