package br.tads.ufpr.bantads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserLogin(@NotEmpty @Email String email, @NotEmpty @Length(min = 12) String password) {
}
