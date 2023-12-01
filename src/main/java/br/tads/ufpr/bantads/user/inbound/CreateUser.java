package br.tads.ufpr.bantads.user.inbound;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreateUser(@NotEmpty @Length(min = 3, max = 255) String firstName,
                         @NotEmpty @Length(min = 3, max = 255) String lastName,
                         @NotEmpty @Length(min = 3, max = 255) @Email String email,
                         @NotEmpty @Length(min = 12) String password) {
}
