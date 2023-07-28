package br.tads.ufpr.bantadsbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "disabled_on")
    private LocalDateTime disabledOn;
    @Column(name = "is_disabled")
    private boolean isDisabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isDisabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isDisabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isDisabled;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled;
    }
}
