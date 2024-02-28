package org.example.authentication.security;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.data.UserEntity;
import org.example.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Component
public class MongoUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public MongoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userResult = userRepository.findByUsername(username);
        if (userResult.isEmpty()) {
            log.info("Could not find a username '{}'.", username);
            throw new UsernameNotFoundException(MessageFormat.format("Could not find a username {0}.", username));
        }
        UserEntity userEntity = userResult.get();
        return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getAuthorities());
    }


}
