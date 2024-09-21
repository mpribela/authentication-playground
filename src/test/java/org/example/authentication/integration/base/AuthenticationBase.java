package org.example.authentication.integration.base;

import org.example.authentication.data.UserEntity;
import org.example.authentication.dto.TokenDto;
import org.example.authentication.integration.base.configuration.PasswordConfiguration;
import org.example.authentication.repository.BookRepository;
import org.example.authentication.repository.UserRepository;
import org.example.authentication.transformer.BookTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.example.authentication.builder.EntityBuilder.createUser;
import static org.example.authentication.data.UserRole.ROLE_ADMIN;
import static org.example.authentication.data.UserRole.ROLE_READER;

@Import({PasswordConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class AuthenticationBase extends DatabaseBase {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected BookTransformer bookTransformer;

    protected static final String EXPIRED_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ0ZXN0LWlzc3VlciIsImlhdCI6MTcyMzM5ODA3MywiZXhwIjoxNzIzNDAxNjczLCJ1c2VyIjoiYWRtaW4iLCJ1c2VySWQiOiI2NmI4ZjdiNmJkNWEyMDIzNmJjMzQ4ZWQiLCJyb2xlcyI6WyJST0xFX0FETUlOIl0sImlzRW5hYmxlZCI6dHJ1ZX0.nWLV7BA5QCzefX9eQowuL0U616uH57vTDasU-bayppzQnDDGEVEEIBV_2OGIBfbsQ95IcBhGqsh36navnckptg_HijwuMmWoDDxIKaEIIvCYmkSpJGn4mdBsgVk6YbCfqhkf5qxF5fIe2qlRjnKMYgnZhuXgXpwgjZcwlWsrmXZ_0ejzA5o_QT_7ZOVdo078rkGaIPRfd-Y5XHqCXEoRri8_2SvlBGuDAtZiljtHnMj-PNYTmcnT2wj92OiqHRvkj0t9qKRtYsQnaQnDNH5O52oJNoukUqUWoWlXXXWIhL3jx-yYANfLtJt7Ca6s_FL7ZmeARRmeQnVLinG-JM0o5Q";

    protected UserEntity reader = createUser().username("reader").userRoles(List.of(ROLE_READER)).build();
    protected UserEntity admin = createUser().username("admin").userRoles(List.of(ROLE_ADMIN)).build();
    protected UserEntity director = createUser().username("director").userRoles(List.of(ROLE_ADMIN, ROLE_READER)).build();

    protected String login(UserEntity user) {
        userRepository.save(user);
        TokenDto tokenDto = testRestTemplate
                .withBasicAuth(user.getUsername(), user.getPassword())
                .postForEntity("/user/login", null, TokenDto.class)
                .getBody();
        return tokenDto.getJwt();
    }

    protected void cleanDatabase() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

}
