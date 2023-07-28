package br.tads.ufpr.bantadsbackend.domain.dto;

import br.tads.ufpr.bantadsbackend.domain.model.UserType;

public record LoginResponseDTO(String token, UserType userType) {
}
