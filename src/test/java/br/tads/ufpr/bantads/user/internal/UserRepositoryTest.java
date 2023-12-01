package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.utils.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should find user by email")
    void findUserByEmail() {
        repository.save(UserBuilder.of());
        assertTrue(repository.findUserByEmail(UserBuilder.of().getEmail()).isPresent());
    }
}