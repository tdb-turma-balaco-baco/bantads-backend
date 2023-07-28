package br.tads.ufpr.bantadsbackend.controller;

import br.tads.ufpr.bantadsbackend.domain.dto.LoginRequestDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.LoginResponseDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.UserRegisterRequestDTO;
import br.tads.ufpr.bantadsbackend.domain.dto.UserRegisterResponseDTO;
import br.tads.ufpr.bantadsbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO login) {
        return ResponseEntity.ok(service.login(login));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody @Valid UserRegisterRequestDTO user) {
        UserRegisterResponseDTO response = service.register(user);
        return ResponseEntity.created(URI.create("/cadastro/" + response.id())).build();
    }
}
