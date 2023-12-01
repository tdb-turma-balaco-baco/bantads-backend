package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.inbound.UpdateUser;
import br.tads.ufpr.bantads.user.internal.User;
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

import java.util.Optional;

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
    @DisplayName("should find list of saved users")
    void findAllUsers() {
        service.create(createUser);
        service.create(new CreateUser("firstName",  "lastName", "email1@email.com", "password"));
        service.create(new CreateUser("firstName",  "lastName", "email2@email.com", "password"));

        var allUsers = service.findAllUsers();
        assertEquals(allUsers.size(), 3);
    }

    @Test
    @DisplayName("should create successfully a user, with encoded password")
    void create() {
        service.create(createUser);
        assertEquals(service.findAllUsers().size(), 1);

        String password = repository.findAll().get(0).getPassword();
        assertTrue(new BCryptPasswordEncoder().matches("password", password));
    }

    @Test
    @DisplayName("should not create user with existing email")
    public void createWithException() {
        assertEquals(service.findAllUsers().size(), 0);
        assertThrows(DataIntegrityViolationException.class, () -> {
            service.create(createUser);
            service.create(new CreateUser("firstName", "lastName", createUser.email(), "password"));
        });
    }

    @Test
    @DisplayName("should update all user values successfully")
    void update() {
        var created = service.create(createUser);
        service.update(new UpdateUser(created.userId(), "newFirstName", "newLastName", "newEmail@email.com", "newPassword"));

        User user = repository.findById(created.userId()).get();
        assertEquals("newFirstName", user.getFirstName());
        assertEquals("newLastName", user.getLastName());
        assertEquals("newEmail@email.com", user.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("newPassword", user.getPassword()));

        assertTrue(true);
    }

    @Test
    @DisplayName("should update some of the user values")
    void updatePartial() {
        var created = service.create(createUser);
        service.update(new UpdateUser(created.userId(), "newFirstName", null,"newEmail@email.com", null));

        User user = repository.findById(created.userId()).get();
        assertEquals("newFirstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("newEmail@email.com", user.getEmail());
        assertFalse(new BCryptPasswordEncoder().matches("newPassword", user.getPassword()));

        assertTrue(true);
    }
}