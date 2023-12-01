package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.inbound.UpdateUser;
import br.tads.ufpr.bantads.user.inbound.UserLogin;
import br.tads.ufpr.bantads.user.internal.User;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import br.tads.ufpr.bantads.user.outbound.UserResponse;
import br.tads.ufpr.bantads.user.outbound.event.UserCreated;
import br.tads.ufpr.bantads.user.utils.UserBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should find list of saved users")
    void findAllUsers() {
        for (int i = 0; i < 3; i++) {
            User entity = UserBuilder.create();

            entity.setEmail("email" + i + "@email.com");
            repository.save(entity);
        }

        var allUsers = service.findAllUsers();
        assertEquals(3, allUsers.size());
    }

    @Test
    @DisplayName("should create successfully a user, with encoded password")
    void create() {
        service.create(createUser);
        assertEquals(service.findAllUsers().size(), 1);

        String password = repository.findAll().get(0).getPassword();
        assertTrue(passwordEncoder.matches("password", password));
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
        var created = repository.save(UserBuilder.create());
        UpdateUser update = new UpdateUser(created.getId(), "newFirstName", "newLastName", "newEmail@email.com", "newPassword");

        service.update(update);
        User user = repository.findById(created.getId()).get();

        assertEquals(update.firstName(), user.getFirstName());
        assertEquals(update.lastName(), user.getLastName());

        assertEquals(update.email().toLowerCase(), user.getEmail());

        assertTrue(passwordEncoder.matches(update.password(), user.getPassword()));
    }

    @Test
    @DisplayName("should update some of the user values")
    void updatePartial() {
        var created = service.create(createUser);

        UpdateUser update = new UpdateUser(created.userId(), "newFirstName", null, "newEmail@email.com", null);
        service.update(update);
        User user = repository.findById(created.userId()).get();

        // updated values
        assertEquals(update.firstName(), user.getFirstName());
        assertEquals(update.email().toLowerCase(), user.getEmail());

        // non updated values
        assertNotEquals(update.lastName(), user.getLastName());
        assertEquals(createUser.lastName(), user.getLastName());
        assertTrue(passwordEncoder.matches(createUser.password(), user.getPassword()));
    }

    @Test
    @DisplayName("should return user response when login successful")
    public void login() {
        UserCreated created = service.create(createUser);

        UserResponse userResponse = service.userLogin(new UserLogin(createUser.email(), createUser.password()));

        assertNotNull(userResponse);
        assertEquals(created.userId(), userResponse.userId());
    }

    @Test
    @DisplayName("should not return user response when login with incorrect info")
    public void givenInvalidLoginShouldNotFind() {
        service.create(createUser);
        var response = service.userLogin(new UserLogin(createUser.email(), "invalid"));
        assertNull(response);

        assertThrows(RuntimeException.class, () -> service.userLogin(new UserLogin("invalid@email.com", createUser.password())));
    }
}