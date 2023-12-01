package br.tads.ufpr.bantads.manager.outbound;

public record ManagerResponse(Long id, String firstName, String lastName, String email, String cpf, String phone) {
}
