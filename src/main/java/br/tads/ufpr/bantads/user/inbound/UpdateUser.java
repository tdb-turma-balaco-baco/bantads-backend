package br.tads.ufpr.bantads.user.inbound;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record UpdateUser(@NotEmpty @Positive Long userId,
                         @Length(min = 3, max = 255) String firstName,
                         @Length(min = 3, max = 255) String lastName,
                         @Length(min = 3, max = 255) @Email String email,
                         @Length(min = 12) String password) {
}
