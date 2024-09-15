package org.example.authentication.service;

import org.example.authentication.builder.EntityBuilder;
import org.example.authentication.data.UserEntity;
import org.example.authentication.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService service;

    String username = "user";

    @Test
    @DisplayName("when username is found then return entity")
    void loadUserByUsernameTest1() {
        //given
        UserEntity databaseUser = EntityBuilder.createUser()
                .username(username)
                .build();
        UserEntity expectedUser = EntityBuilder.createUser()
                .username(username)
                .build();
        given(userRepository.findByUsername(username)).willReturn(Optional.of(databaseUser));


        //when
        UserDetails resultUser = service.loadUserByUsername(username);

        //then
        assertEquals(expectedUser, resultUser);
    }

    @Test
    @DisplayName("when username is not found then throw exception")
    void loadUserByUsernameTest2() {
        //given
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when then
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));
    }

    @Test
    @DisplayName("when username is null then throw exception")
    void loadUserByUsernameTest3() {
        //given
        given(userRepository.findByUsername(isNull())).willReturn(Optional.empty());

        //when then
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(null));
    }

}