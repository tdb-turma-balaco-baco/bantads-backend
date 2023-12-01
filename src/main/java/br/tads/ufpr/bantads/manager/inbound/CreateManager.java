package br.tads.ufpr.bantads.manager.inbound;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreateManager(@NotEmpty @Length(min = 3, max = 255) String firstName,
                            @NotEmpty @Length(min = 3, max = 255) String lastName,
                            @NotEmpty @Email String email,
                            @NotEmpty @Length(min = 11, max = 11) String cpf,
                            @NotEmpty @Length(min = 11, max = 11) String phone) {
}
