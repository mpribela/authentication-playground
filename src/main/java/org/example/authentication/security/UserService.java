package org.example.authentication.security;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.data.UserEntity;
import org.example.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Component
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username);
    }

    public UserEntity getUserEntityByUsername(String username) {
        return getUser(username);
    }

    private UserEntity getUser(String username) {
        Optional<UserEntity> userResult = userRepository.findByUsername(username);
        return userResult.orElseThrow(() -> {
            log.info("Could not find a username '{}'.", username);
            return new UsernameNotFoundException(MessageFormat.format("Could not find a username {0}.", username));
        });
    }


}
