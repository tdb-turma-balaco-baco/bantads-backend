package br.tads.ufpr.bantadsbackend.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
