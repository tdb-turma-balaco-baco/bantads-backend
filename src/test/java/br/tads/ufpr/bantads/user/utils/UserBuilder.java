package br.tads.ufpr.bantads.user.utils;

import br.tads.ufpr.bantads.user.internal.User;

public class UserBuilder {
    public static User create() {
        var user = new User();

        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setEmail("email@email.com");
        user.setPassword("password");

        return user;
    }
}
