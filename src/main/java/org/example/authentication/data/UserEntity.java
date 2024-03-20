package org.example.authentication.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "password")
@Document("user")
public class UserEntity implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private List<UserRole> userRoles;
    private OffsetDateTime created;
    private OffsetDateTime lastLogin;
    private boolean enabled;

    public UserEntity(String id, String username, List<String> userRoles, boolean isEnabled) {
        this.id = id;
        this.username = username;
        this.userRoles = userRoles.stream().map(UserRole::fromText).toList();
        this.enabled = isEnabled;
        this.password = "";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        userRoles.forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    //fixme tmp
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //fixme tmp
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //fixme tmp
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
