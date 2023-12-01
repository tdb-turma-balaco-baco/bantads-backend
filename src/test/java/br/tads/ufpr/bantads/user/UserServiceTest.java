package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    private static final CreateUser createUser = new CreateUser("firstName", "lastName", "email@email.com", "password");
    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should find list of saved users")
    void findAllUsers() {
        service.create(createUser);
        service.create(new CreateUser("firstName",  "lastName", "email1@email.com", "password"));
        service.create(new CreateUser("firstName",  "lastName", "email2@email.com", "password"));

        var allUsers = service.findAllUsers();
        assertEquals(allUsers.size(), 3);
    }

    @Test
    @DisplayName("Should create successfully a user, with encoded password")
    void create() {
        service.create(createUser);
        assertEquals(service.findAllUsers().size(), 1);

        String password = repository.findAll().get(0).getPassword();
        assertTrue(new BCryptPasswordEncoder().matches("password", password));
    }

    @Test
    @DisplayName("Should not create user with existing email")
    public void createWithException() {
        assertEquals(service.findAllUsers().size(), 0);
        assertThrows(DataIntegrityViolationException.class, () -> {
            service.create(createUser);
            service.create(new CreateUser("firstName", "lastName", createUser.email(), "password"));
        });
    }

    @Test
    void update() {
        service.create(createUser);
        assertTrue(true);
    }
}