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
    private static final User user = UserBuilder.create();

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should create a new user successfully")
    void createUser() {
        repository.save(user);
        assertEquals(repository.count(), 1);
    }

    @Test
    @DisplayName("should update a user successfully")
    void updateUser() {
        User saved = repository.save(user);
        User update = UserBuilder.create();

        update.setId(saved.getId());
        update.setFirstName("newFirstName");
        repository.saveAndFlush(update);

        assertEquals(repository.count(), 1);
        assertEquals(repository.findById(saved.getId()).get().getFirstName(), update.getFirstName());
    }

    @Test
    @DisplayName("should find user by email")
    void findUserByEmail() {
        repository.save(user);
        assertTrue(repository.findUserByEmail(user.getEmail()).isPresent());
    }
}