package br.tads.ufpr.bantads.user.outbound;

public record UserResponse(Long userId, String firstName, String lastName, String email) {
}
