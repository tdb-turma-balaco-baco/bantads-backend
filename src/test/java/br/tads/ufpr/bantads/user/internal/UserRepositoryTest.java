package br.tads.ufpr.bantads.user.internal;

import org.junit.jupiter.api.BeforeEach;
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
    private static final User user = createMockUser();

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private static User createMockUser() {
        var user = new User();

        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setEmail("email@email.com");
        user.setPassword("password");

        return user;
    }

    @Test
    void itShouldCreateUser() {
        repository.save(user);
        assertEquals(repository.findAll().size(), 1);
    }
}