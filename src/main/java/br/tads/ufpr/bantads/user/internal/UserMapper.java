package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.UserResponse;

import java.util.function.Function;

public class UserMapper {
    public static final Function<User, UserResponse> toResponse = (user) -> new UserResponse(
            user.getId(),
            user.getEmail()
    );
}
