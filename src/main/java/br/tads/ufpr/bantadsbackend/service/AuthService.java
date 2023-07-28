package br.tads.ufpr.bantadsbackend.service;

import br.tads.ufpr.bantadsbackend.domain.dto.LoginRequestDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.LoginResponseDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.UserRegisterRequestDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.UserRegisterResponseDTO;
import br.tads.ufpr.bantadsbackend.domain.model.User;
import br.tads.ufpr.bantadsbackend.domain.model.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        authenticationManager.authenticate(token);

        var user = userService.findUserByEmail(request.email());
        String jwt = jwtService.createToken(user);

        return new LoginResponseDTO(jwt, user.getUserType());
    }

    public UserRegisterResponseDTO register(UserRegisterRequestDTO request) {
        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdOn(LocalDateTime.now())
                .userType(UserType.CLIENT)
                .build();

        var createdUser = userService.register(user);
        return new UserRegisterResponseDTO(createdUser.getId());
    }
}
