package br.tads.ufpr.bantads.user.utils;

import br.tads.ufpr.bantads.user.internal.User;

public class UserBuilder {
    private final User user;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static User of() {
        return new UserBuilder().build();
    }

    private UserBuilder() {
        this.user = new User();
        this.user.setEmail("email@email.com");
        this.user.setPassword("random");
    }

    public UserBuilder email(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder password(String password) {
        this.user.setPassword(password);
        return this;
    }

    public User build() {
        return this.user;
    }
}
