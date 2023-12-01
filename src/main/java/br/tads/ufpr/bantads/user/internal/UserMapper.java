package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.outbound.UserResponse;

import java.util.function.Function;

public class UserMapper {

    public static final Function<CreateUser, User> createUserToEntity = (createUser) -> {
        User user = new User();

        user.setFirstName(createUser.firstName());
        user.setLastName(createUser.lastName());
        user.setEmail(createUser.email().toLowerCase());
        user.setPassword(createUser.password());

        return user;
    };

    public static final Function<User, UserResponse> toResponse = (user) -> new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
    );
}
